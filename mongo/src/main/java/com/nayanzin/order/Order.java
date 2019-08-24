package com.nayanzin.order;

import com.nayanzin.address.Address;
import com.nayanzin.data.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document
public class Order extends BaseEntity {

    @Id
    private String orderId;

    private String accountNumber;

    private OrderStatus orderStatus;

    private Address shippingAddress;

    private List<LineItem> lineItems = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    private BigDecimal totalAmount;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private Point coordinates;

    public boolean addLineItem(LineItem lineItem) {
        if (isNull(this.lineItems)) {
            this.lineItems = new ArrayList<>();
        }
        return this.lineItems.add(lineItem);
    }

    @EqualsAndHashCode.Include
    public BigDecimal totalAmountForEqualsAndHashcode() {
        return nonNull(totalAmount) ? totalAmount.stripTrailingZeros() : null;
    }
}
