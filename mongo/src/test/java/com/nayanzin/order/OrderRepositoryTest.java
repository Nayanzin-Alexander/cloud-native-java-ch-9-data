package com.nayanzin.order;

import com.nayanzin.aggregation.AccountOrdersAggregation;
import com.nayanzin.data.MongoConversionConfig;
import com.nayanzin.data.MongoListenersConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static com.nayanzin.order.OrderTestUtil.getOrder;
import static java.math.BigDecimal.*;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@Import({MongoConversionConfig.class, MongoListenersConfig.class})
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void preconditions() {
        // Remove previous orders.
        orderRepository.deleteAll();

        // Save 20 orders.
        orderRepository.saveAll(IntStream
                .range(0, 20)
                .mapToObj(i -> getOrder())
                .collect(toList()));
    }

    @Test
    public void saveOrderTest() {
        // Given an order.
        Order order = getOrder();

        // When save order.
        order = orderRepository.save(order);

        // Then order is accessible from the repository.
        assertThat(order.getOrderId(), notNullValue());
        Order savedOrder = orderRepository.findById(order.getOrderId()).orElse(null);
        assertThat(savedOrder, notNullValue());
    }

    @Test
    public void pagingOrdersTest() {
        // When get page of orders.
        Page<Order> ordersPage = orderRepository.findAll(PageRequest.of(0, 10));

        // Then orders page is correct.
        assertThat(ordersPage.isFirst(), is(true));
        assertThat(ordersPage.getNumber(), comparesEqualTo(0));
        assertThat(ordersPage.getSize(), comparesEqualTo(10));
        assertThat(ordersPage.getContent(), hasSize(10));
        assertThat(ordersPage.getTotalPages(), comparesEqualTo(2));
        assertThat(ordersPage.getTotalElements(), comparesEqualTo(20L));
    }

    @Test
    public void findByAccountNumberTest() {
        // Given saved order with $accountNumber.
        String accountNumber = "654321";
        Order order = getOrder(accountNumber);
        order = orderRepository.save(order);

        // When find order by account number.
        List<Order> foundOrdersList = orderRepository.findByAccountNumber(accountNumber);

        // Then order returns.
        assertThat(foundOrdersList, hasSize(1));
        Order foundOrder = foundOrdersList.get(0);

        assertThat(foundOrder.getOrderId(), is(order.getOrderId()));
        assertThat(foundOrder.getAccountNumber(), is(order.getAccountNumber()));
        assertThat(foundOrder.getOrderStatus(), is(order.getOrderStatus()));
        assertThat(foundOrder.getShippingAddress(), is(order.getShippingAddress()));
        assertThat(foundOrder.getLineItems(), is(order.getLineItems()));
        assertThat(foundOrder.getTotalAmount(), is(order.getTotalAmount()));
        assertThat(foundOrder.getCoordinates(), is(order.getCoordinates()));
        assertThat(foundOrder.getCreatedAt(), notNullValue());
        assertThat(foundOrder.getLastModified(), notNullValue());
        assertThat(foundOrder, is(order));

        assertThat(foundOrder.getAccountNumber(), is(accountNumber));
    }

    @Test
    public void findByTotalAmountBetweenTest() {
        // When find by between nonexistent total amount then no orders returns.
        assertThat(orderRepository.findByTotalAmountBetween(ONE, TEN),
                hasSize(0));

        // When find by between existent total amount then 20 orders returns.
        assertThat(orderRepository.findByTotalAmountBetween(BigDecimal.valueOf(60), BigDecimal.valueOf(70)),
                hasSize(20));
    }

    @Test
    public void getByLineItemNameContainsTest() {
        // When find by between nonexistent line item name then no orders returns.
        assertThat(orderRepository.getByLineItemNameContains("Spring"),
                hasSize(0));

        // When find by between existent line item name then 20 orders returns.
        assertThat(orderRepository.getByLineItemNameContains("NoSql"),
                hasSize(20));
    }

    @Test
    public void findByCoordinatesNearTest() {
        final Point coordinates = new Point(-122.125792, 37.447224);
        final Distance oneKm = new Distance(1, Metrics.KILOMETERS);
        final Distance tenMiles = new Distance(10, Metrics.MILES);

        // When find by small radius then no orders returns.
        assertThat(orderRepository.findByCoordinatesNear(coordinates, oneKm),
                hasSize(0));

        // When find by sufficient radius then orders returns.
        assertThat(orderRepository.findByCoordinatesNear(coordinates, tenMiles),
                hasSize(20));
    }

    @Test
    public void mongoTemplateFindByAccountNumberTest() {
        // Given find orders by account number query
        Query query = new Query();
        query.addCriteria(where("accountNumber").is("account_1"));

        // When execute query.
        List<Order> orders = mongoTemplate.find(query, Order.class);

        // Then orders return.
        assertThat(orders, hasSize(20));
    }

    @Test
    public void projectionTest() {
        // Given query with field excludes.
        Query query = new Query();
        query.fields()
                .exclude("shippingAddress")
                .exclude("lineItems");

        // When execute query.
        List<Order> orders = mongoTemplate.find(query, Order.class);

        // Then orders return.
        orders.forEach(order -> {
            assertThat(order.getShippingAddress(), is(nullValue()));
            assertThat(order.getLineItems(), hasSize(0));
        });
    }

    @Test
    public void aggregationTotalSumTest() {
        // Given 20 orders saved in @Before method for the first account.
        // And additional 30 orders for the second account.
        orderRepository.saveAll(IntStream
                .range(0, 30)
                .mapToObj(i -> getOrder("account_2"))
                .collect(toList()));
        // And additional 40 orders for the third account.
        orderRepository.saveAll(IntStream
                .range(0, 40)
                .mapToObj(i -> getOrder("account_3"))
                .collect(toList()));

        // When get account total sum of pending orders using aggregateion.
        MatchOperation matchByPendingStatus = Aggregation.match(new Criteria("orderStatus").is(OrderStatus.PENDING));

        GroupOperation groupByAccountNumber = Aggregation
                .group("accountNumber")
                .sum("totalAmount").as("accountTotalAmount")
                .avg("totalAmount").as("accountAverageAmount");

        SortOperation sort = Aggregation.sort(new Sort(ASC, "_id"));

        Aggregation aggregation = Aggregation.newAggregation(
                matchByPendingStatus,
                groupByAccountNumber,
                sort
        );

        AggregationResults<AccountOrdersAggregation> sortedResult = mongoTemplate.aggregate(aggregation, "order", AccountOrdersAggregation.class);

        // Then valid result.
        List<AccountOrdersAggregation> sortedAggregations = sortedResult.getMappedResults();
        assertThat(sortedAggregations, hasSize(3));

        // First account order aggregation is correct.
        AccountOrdersAggregation acc1 = sortedAggregations.get(0);
        assertThat(acc1.getId(), is("account_1"));
        assertThat(acc1.getAccountTotalAmount(), is(closeTo(BigDecimal.valueOf(1382.0), ZERO)));
        assertThat(acc1.getAccountAverageAmount(), is(closeTo(BigDecimal.valueOf(69.1), ZERO)));

        // Second account order aggregation is correct.
        AccountOrdersAggregation acc2 = sortedAggregations.get(1);
        assertThat(acc2.getId(), is("account_2"));
        assertThat(acc2.getAccountTotalAmount(), is(closeTo(BigDecimal.valueOf(2073.0), ZERO)));
        assertThat(acc2.getAccountAverageAmount(), is(closeTo(BigDecimal.valueOf(69.1), ZERO)));

        // Third account order aggregation is correct.
        AccountOrdersAggregation acc3 = sortedAggregations.get(2);
        assertThat(acc3.getId(), is("account_3"));
        assertThat(acc3.getAccountTotalAmount(), is(closeTo(BigDecimal.valueOf(2764.0), ZERO)));
        assertThat(acc3.getAccountAverageAmount(), is(closeTo(BigDecimal.valueOf(69.1), ZERO)));
    }

    // TODO create more sufficient aggregate and map-reduce cases.
    // TODO how about to use Money API instead of using BigDecimal

}