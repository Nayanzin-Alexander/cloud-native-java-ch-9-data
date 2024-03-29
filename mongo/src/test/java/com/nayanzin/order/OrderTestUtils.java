package com.nayanzin.order;

import com.nayanzin.address.Address;
import com.nayanzin.address.AddressType;
import org.springframework.data.geo.Point;

import java.math.BigDecimal;
import java.util.Arrays;

public abstract class OrderTestUtils {

    public static Order getOrder() {
        return getOrder("account_1");
    }

    static Order getOrder(String accountNumber) {
        return Order.builder()
                .accountNumber(accountNumber)
                .orderStatus(OrderStatus.PENDING)
                .shippingAddress(Address.builder()
                        .addressType(AddressType.SHIPPING)
                        .street1("655 Campus Drive")
                        .city("Stanford")
                        .state("CA")
                        .zipCode(94305)
                        .country("USA")
                        .build())
                .lineItems(Arrays.asList(
                        LineItem.builder()
                                .name("Cloud Native Java")
                                .productId("000001")
                                .quantity(1)
                                .price(new BigDecimal("34.55"))
                                .tax(new BigDecimal("15"))
                                .build(),
                        LineItem.builder()
                                .name("NoSql Distilled")
                                .productId("000002")
                                .quantity(1)
                                .price(new BigDecimal("34.55"))
                                .tax(new BigDecimal("15"))
                                .build()
                ))
                .totalAmount(new BigDecimal("69.1"))
                .coordinates(new Point(-122.161772, 37.429290))
                .build();
    }
}
