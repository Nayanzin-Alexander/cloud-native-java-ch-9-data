package com.nayanzin.order;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class OrderMongoTemplateRepositoryImpl implements OrderMongoTemplateRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Order> mongoTemplateFindByAccountNumber(String accountNumber) {
        Query query = new Query(where("accountNumber").is(accountNumber));
        return mongoTemplate.find(query, Order.class);
    }

    @Override
    public List<Order> mongoTemplateFindWithExcludedFields(String... fields) {
        Query query = new Query();
        Arrays.stream(fields)
                .filter(Objects::nonNull)
                .forEach(f -> query.fields().exclude(f));
        return mongoTemplate.find(query, Order.class);
    }

    @Override
    public List<AccountOrdersAggregation> mongoTemplateAggregateOrdersByAccountFilterByStatus(OrderStatus status) {
        MatchOperation matchByStatus = Aggregation.match(new Criteria("orderStatus").is(status));

        GroupOperation groupByAccountNumber = Aggregation
                .group("accountNumber")
                .sum("totalAmount").as("accountTotalAmount")
                .avg("totalAmount").as("accountAverageAmount");

        SortOperation sortByIdAsc = Aggregation.sort(new Sort(ASC, "_id"));

        Aggregation aggregation = Aggregation.newAggregation(
                matchByStatus,
                groupByAccountNumber,
                sortByIdAsc
        );

        return mongoTemplate.aggregate(aggregation, "order", AccountOrdersAggregation.class)
                .getMappedResults();
    }
}
