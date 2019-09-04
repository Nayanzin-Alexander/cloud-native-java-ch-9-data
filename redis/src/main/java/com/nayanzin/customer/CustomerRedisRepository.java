package com.nayanzin.customer;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRedisRepository extends CrudRepository<Customer, Long> {

    Customer findByUserId(String userId);

    List<Customer> findByAccountsId(Long id);
}
