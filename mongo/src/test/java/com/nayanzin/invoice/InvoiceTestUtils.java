package com.nayanzin.invoice;

import com.nayanzin.address.Address;

import java.util.Arrays;

import static com.nayanzin.order.OrderTestUtils.getOrder;

abstract class InvoiceTestUtils {

    static Invoice getInvoice(Address billingAddress) {
        return Invoice.builder()
                .customerId("customer_1")
                .orders(Arrays.asList(
                        getOrder(),
                        getOrder()
                ))
                .billingAddress(billingAddress)
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();
    }
}
