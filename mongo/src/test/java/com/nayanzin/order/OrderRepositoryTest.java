package com.nayanzin.order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static com.nayanzin.order.OrderTestUtil.getOrder;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
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
        Order order = getOrder();
        order.setAccountNumber(accountNumber);
        order = orderRepository.save(order);

        // When find order by account number.
        List<Order> foundOrdersList = orderRepository.findByAccountNumber(accountNumber);

        // Then order returns.
        assertThat(foundOrdersList, hasSize(1));
        Order foundOrder = foundOrdersList.get(0);
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
        query.addCriteria(where("accountNumber").is("123456"));

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
}