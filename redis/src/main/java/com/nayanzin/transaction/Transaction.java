package com.nayanzin.transaction;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@RedisHash("transaction")
public class Transaction {

    @Id
    private Long id;

    private BigDecimal amount;

    private Date date;

    @Indexed
    private Long debitAccountId;

    @Indexed
    private Long creditAccountId;
}
