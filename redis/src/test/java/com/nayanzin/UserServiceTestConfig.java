package com.nayanzin;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class UserServiceTestConfig {

    // https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/
    private static final GenericContainer POSTGRES_CONTAINER;
    private static final GenericContainer REDIS_CONTAINER;
    static {
        POSTGRES_CONTAINER = new FixedHostPortGenericContainer("postgres")
                .withFixedExposedPort(5434, 5432)
                .withEnv("POSTGRES_USER", "test_user")
                .withEnv("POSTGRES_PASSWORD", "test_password")
                .withEnv("POSTGRES_DB", "test_db")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 1));
        POSTGRES_CONTAINER.start();

        REDIS_CONTAINER = new FixedHostPortGenericContainer("redis")
                .withFixedExposedPort(6380, 6379)
                .waitingFor(Wait.forLogMessage(".*Ready to accept connections*\\s", 1));
        REDIS_CONTAINER.start();
    }
}