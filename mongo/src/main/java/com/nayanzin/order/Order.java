package com.nayanzin.order;

import com.nayanzin.address.Address;
import com.nayanzin.data.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document
public class Order extends BaseEntity {

    @Id
    private String orderId;

    private String accountNumber;

    private OrderStatus orderStatus;

    private Address shippingAddress;

    private List<LineItem> lineItems = new ArrayList<>();

    public boolean addLineItem(LineItem lineItem) {
        if (isNull(this.lineItems)) {
            this.lineItems = new ArrayList<>();
        }
        return this.lineItems.add(lineItem);
    }
}
