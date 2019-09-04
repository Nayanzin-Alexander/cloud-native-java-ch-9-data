package com.nayanzin.customer;

import com.nayanzin.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/*
    Tells Redis to create Hash for storing full Customer object:
        KEY: 'customer:id'
        Pairs:  '_class': 'com.nayanzin.customer.Customer',
                'id': '1',
                'accounts.[0].id': '1',
                'accounts.[0].number': '1234',
                'accounts.[0].balance': '100',
                'accounts.[1].id': '2',
                'accounts.[1].number': '1234',
                'accounts.[1].balance': '100'
 */
@RedisHash("customer")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    /*
        Tells Redis to create SET for storing Customer Ids.
            Key: 'customer'.
            Members: [Customer.id...]
        Think for imitation of uniq functionality.
     */
    @Id
    private long id;

    /*
        Tells Redis to create SET to store customer Ids, where particular userId exists.
            Key: 'customer:userId:value'
            Members: [Customer.id...]
     */
    @Indexed
    private String userId;

    private String name;

    private List<Account> accounts = new ArrayList<>();
}
