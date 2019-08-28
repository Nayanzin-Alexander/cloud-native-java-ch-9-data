package com.nayanzin.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"test"})
public class JdbcApplicationTests {

    private static final GenericContainer POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new FixedHostPortGenericContainer("postgres")
                .withFixedExposedPort(5433, 5432)
                .withEnv("POSTGRES_USER", "test_user")
                .withEnv("POSTGRES_PASSWORD", "test_password")
                .withEnv("POSTGRES_DB", "test_db")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 1));
        POSTGRES_CONTAINER.start();
    }

    @Test
    public void contextLoads() {

    }
}
