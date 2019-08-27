package com.nayanzin.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
class AccountOrdersAggregation {

    /**
     * Account number.
     */
    private String id;

    private BigDecimal accountTotalAmount;

    private BigDecimal accountAverageAmount;
}
