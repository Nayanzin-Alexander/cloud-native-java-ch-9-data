package com.nayanzin.customer;

import com.nayanzin.UserServiceTestConfig;
import com.nayanzin.account.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class CustomerRedisRepositoryTest extends UserServiceTestConfig {

    @Autowired
    private CustomerRedisRepository repository;

    private Customer savedCustomer;

    private Customer savedCustomer2;

    @Before
    public void saveCustomer() {
        savedCustomer = Customer.builder()
                .id(1L)
                .userId("userId")
                .name("name")
                .accounts(Arrays.asList(
                        Account.builder()
                                .id(1L)
                                .number("1234")
                                .balance(BigDecimal.valueOf(100L)).build(),
                        Account.builder()
                                .id(2L)
                                .number("5678")
                                .balance(BigDecimal.valueOf(100L)).build()))
                .build();
        savedCustomer = repository.save(savedCustomer);

        savedCustomer2 = Customer.builder()
                .id(2L)
                .userId("userId2")
                .name("name2")
                .accounts(Arrays.asList(
                        Account.builder()
                                .id(1L)
                                .number("4321")
                                .balance(BigDecimal.valueOf(100L)).build(),
                        Account.builder()
                                .id(3L)
                                .number("5678")
                                .balance(BigDecimal.valueOf(100L)).build()))
                .build();
        savedCustomer2 = repository.save(savedCustomer2);
    }

    @After
    public void removeCustomer() {
        repository.delete(savedCustomer);
    }

    @Test
    public void whenFindByUserIdThenSucceed() {
        Customer customer = repository.findByUserId("userId");
        assertThat(customer, notNullValue());
    }

    @Test
    public void whenFindByAccountIdSucceed() {
        List<Customer> customers = repository.findByAccountsId(1L);
        assertThat(customers, hasSize(2));
    }
}