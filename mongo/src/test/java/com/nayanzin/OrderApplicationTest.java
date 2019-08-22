package com.nayanzin;

import com.nayanzin.address.Address;
import com.nayanzin.address.AddressType;
import com.nayanzin.invoice.InvoiceRepository;
import com.nayanzin.order.LineItem;
import com.nayanzin.order.Order;
import com.nayanzin.order.OrderRepository;
import com.nayanzin.order.OrderStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class OrderApplicationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    //    @Before
//    @After
    public void reset() {
        orderRepository.deleteAll();
        invoiceRepository.deleteAll();
    }

    @Test
    public void orderTest() {
        Address shippingAddress = getShippingAddress();
        Order order = getOrder(shippingAddress);
        order.addLineItem(getLineItem(1));
        order.addLineItem(getLineItem(2));
        order.addLineItem(getLineItem(3));
        order.addLineItem(getLineItem(4));

        orderRepository.save(order);

        assertThat(order.getOrderId(), notNullValue());
        assertThat(order.getLineItems(), hasSize(4));
        assertThat(order.getLastModified(), is(order.getCreatedAt()));

        orderRepository.save(order);
        assertThat(order.getLastModified(), not(order.getCreatedAt()));
    }

    private LineItem getLineItem(int i) {
        return LineItem.builder()
                .name("name_" + i)
                .productId("productId_" + i)
                .quantity(i * i)
                .price(BigDecimal.valueOf(i).pow(2))
                .tax(new BigDecimal("18"))
                .build();
    }

    private Order getOrder(Address shippingAddress) {
        return Order.builder()
                .accountNumber("12345")
                .shippingAddress(shippingAddress)
                .orderStatus(OrderStatus.PENDING)
                .build();
    }

    private Address getShippingAddress() {
        return Address.builder()
                .street1("1600 Pennsylvania Ave NW")
                .state("DC")
                .city("Washington")
                .country("United States")
                .zipCode(20500)
                .addressType(AddressType.SHIPPING)
                .build();
    }
}