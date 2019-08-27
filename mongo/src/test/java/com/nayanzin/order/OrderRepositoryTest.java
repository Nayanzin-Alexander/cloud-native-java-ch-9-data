package com.nayanzin.order;

import com.nayanzin.OrderApplicationTestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static com.nayanzin.order.OrderTestUtils.getOrder;
import static java.math.BigDecimal.*;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class OrderRepositoryTest extends OrderApplicationTestConfig {

    @Autowired
    private OrderRepository orderRepository;

    @Before @After
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
        // When find orders by account  number using mongo template.
        List<Order> orders = orderRepository.mongoTemplateFindByAccountNumber("account_1");

        // Then orders return.
        assertThat(orders, hasSize(20));
    }

    @Test
    public void projectionTest() {
        // When get orders with excluded fields.
        List<Order> orders = orderRepository.mongoTemplateFindWithExcludedFields("shippingAddress", "lineItems");

        // Then orders return with excluded fields.
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

        // When get account total sum of pending orders using aggregation.
        List<AccountOrdersAggregation> results = orderRepository.mongoTemplateAggregateOrdersByAccountFilterByStatus(OrderStatus.PENDING);


        // Then orders are grouped by account and total amounts and account average amounts are valid.
        assertThat(results, hasSize(3));

        // First account order aggregation is correct.
        AccountOrdersAggregation acc1 = results.get(0);
        assertThat(acc1.getId(), is("account_1"));
        assertThat(acc1.getAccountTotalAmount(), is(closeTo(BigDecimal.valueOf(1382.0), ZERO)));
        assertThat(acc1.getAccountAverageAmount(), is(closeTo(BigDecimal.valueOf(69.1), ZERO)));

        // Second account order aggregation is correct.
        AccountOrdersAggregation acc2 = results.get(1);
        assertThat(acc2.getId(), is("account_2"));
        assertThat(acc2.getAccountTotalAmount(), is(closeTo(BigDecimal.valueOf(2073.0), ZERO)));
        assertThat(acc2.getAccountAverageAmount(), is(closeTo(BigDecimal.valueOf(69.1), ZERO)));

        // Third account order aggregation is correct.
        AccountOrdersAggregation acc3 = results.get(2);
        assertThat(acc3.getId(), is("account_3"));
        assertThat(acc3.getAccountTotalAmount(), is(closeTo(BigDecimal.valueOf(2764.0), ZERO)));
        assertThat(acc3.getAccountAverageAmount(), is(closeTo(BigDecimal.valueOf(69.1), ZERO)));
    }
}