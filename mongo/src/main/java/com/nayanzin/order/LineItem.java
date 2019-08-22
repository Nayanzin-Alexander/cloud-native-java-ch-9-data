package com.nayanzin.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItem {

    private String name;

    private String productId;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal tax;
}
