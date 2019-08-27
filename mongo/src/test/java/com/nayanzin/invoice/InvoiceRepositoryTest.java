package com.nayanzin.invoice;

import com.nayanzin.OrderApplicationTestConfig;
import com.nayanzin.address.Address;
import com.nayanzin.address.AddressType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.nayanzin.invoice.InvoiceTestUtils.getInvoice;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class InvoiceRepositoryTest  extends OrderApplicationTestConfig {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Before @After
    public void cleanUp() {
        invoiceRepository.deleteAll();
    }

    @Test
    public void findByBillingAddressTest() {
        // Given saved invoice.
        Address billingAddress = Address.builder()
                .addressType(AddressType.SHIPPING)
                .street1("450 Serra Mall")
                .city("Stanford")
                .state("CA")
                .zipCode(94305)
                .country("USA")
                .build();
        Invoice invoice = getInvoice(billingAddress);
        invoiceRepository.save(invoice);

        // When find invoice by billing address.
        List<Invoice> foundInvoices = invoiceRepository.findByBillingAddress(billingAddress);

        // Then invoice returns.
        assertThat(foundInvoices, hasSize(1));
        Invoice foundInvoice = foundInvoices.get(0);
        assertThat(foundInvoice, notNullValue());
        assertThat(foundInvoice.getInvoiceId(), notNullValue());
        assertThat(foundInvoice.getCustomerId(), is(invoice.getCustomerId()));
        assertThat(foundInvoice.getOrders(), is(invoice.getOrders()));
        assertThat(foundInvoice.getBillingAddress(), is(invoice.getBillingAddress()));
        assertThat(foundInvoice.getInvoiceStatus(), is(invoice.getInvoiceStatus()));
    }

}