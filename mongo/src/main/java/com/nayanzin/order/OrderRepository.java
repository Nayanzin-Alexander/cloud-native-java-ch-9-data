package com.nayanzin.order;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends OrderMongoTemplateRepository, PagingAndSortingRepository<Order, String> {

    // Query methods.
    List<Order> findByAccountNumber(String accountNumber);

    List<Order> findByTotalAmountBetween(BigDecimal from, BigDecimal to);

    List<Order> findByCoordinatesNear(Point point, Distance distance);

    // MongoDB JSON-based Query Methods
    @Query("{ 'lineItems.name' : {$regex : ?0}}")
    List<Order> getByLineItemNameContains(String lineItemName);
}
