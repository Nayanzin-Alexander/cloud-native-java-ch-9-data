package com.nayanzin.order;

import java.util.List;

public interface OrderMongoTemplateRepository {
    List<Order> mongoTemplateFindByAccountNumber(String accountNumber);

    List<Order> mongoTemplateFindWithExcludedFields(String... fields);

    List<AccountOrdersAggregation> mongoTemplateAggregateOrdersByAccountFilterByStatus(OrderStatus status);
}
