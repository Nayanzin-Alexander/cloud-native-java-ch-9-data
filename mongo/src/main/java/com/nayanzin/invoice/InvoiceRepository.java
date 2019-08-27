package com.nayanzin.invoice;

import com.nayanzin.address.Address;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, String> {

    List<Invoice> findByBillingAddress(Address address);
}
