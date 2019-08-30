package com.nayanzin.order;

import lombok.*;

import java.math.BigDecimal;

import static java.util.Objects.nonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class LineItem {

    private String name;

    private String productId;

    private Integer quantity;

    @EqualsAndHashCode.Exclude
    private BigDecimal price;

    @EqualsAndHashCode.Exclude
    private BigDecimal tax;

    @SuppressWarnings("unused")
    @EqualsAndHashCode.Include
    public BigDecimal priceForEqualsAndHashcode() {
        return nonNull(price) ? price.stripTrailingZeros() : null;
    }

    @SuppressWarnings("unused")
    @EqualsAndHashCode.Include
    public BigDecimal taxForEqualsAndHashcode() {
        return nonNull(tax) ? tax.stripTrailingZeros() : null;
    }
}
