package com.nayanzin.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.index.Indexed;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    /*
        Tells Redis to create Set to store customer Ids where particular account id exists.
            Key: customer:accounts.id:1
            Members: [Customer.id...]
     */
    @Indexed
    private Long id;

    private String number;

    private BigDecimal balance;
}
