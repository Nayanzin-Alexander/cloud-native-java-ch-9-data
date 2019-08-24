package com.nayanzin.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

@Configuration
public class MongoListenersConfig {

    @Bean
    public AbstractMongoEventListener beforeSaveListener() {
        return new BeforeSaveListener();
    }
}
