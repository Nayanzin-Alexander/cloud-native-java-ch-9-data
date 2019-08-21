package com.nayanzin.jdbc;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"test"})
public class JdbcApplicationTests {

    @ClassRule
    public static DockerComposeContainer env = new DockerComposeContainer(new File("src/test/resources/compose-test.yml"))
                .withExposedService("postgres_1", 5432);

    @Test
    public void contextLoads() {
        System.out.println("Hello world");
    }

}
