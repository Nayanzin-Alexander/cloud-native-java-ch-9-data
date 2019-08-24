package com.nayanzin.aggregation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountOrdersAggregation {

    /**
     * Account number.
     */
    private String id;

    private BigDecimal accountTotalAmount;

    private BigDecimal accountAverageAmount;
}
