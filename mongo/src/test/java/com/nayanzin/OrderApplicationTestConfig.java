package com.nayanzin;

import com.nayanzin.data.MongoConversionConfig;
import com.nayanzin.data.MongoListenersConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@Import({MongoConversionConfig.class, MongoListenersConfig.class})
public abstract class OrderApplicationTestConfig {

    // https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/
    private static final GenericContainer MONGO_CONTAINER;
    static {
        MONGO_CONTAINER = new FixedHostPortGenericContainer("mongo")
            .withFixedExposedPort(27018, 27017);
        MONGO_CONTAINER.start();}
}