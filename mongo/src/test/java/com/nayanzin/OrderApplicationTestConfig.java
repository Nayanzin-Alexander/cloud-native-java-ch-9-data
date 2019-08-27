package com.nayanzin;

import com.nayanzin.address.Address;
import com.nayanzin.address.AddressType;
import com.nayanzin.data.MongoConversionConfig;
import com.nayanzin.data.MongoListenersConfig;
import com.nayanzin.invoice.InvoiceRepository;
import com.nayanzin.order.LineItem;
import com.nayanzin.order.Order;
import com.nayanzin.order.OrderRepository;
import com.nayanzin.order.OrderStatus;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@Import({MongoConversionConfig.class, MongoListenersConfig.class})
public abstract class OrderApplicationTestConfig {

    @ClassRule
    public static DockerComposeContainer env = new DockerComposeContainer(new File("src/test/resources/compose-test.yml"))
            .withExposedService("test-mongo", 27018);

    // TODO add usage of mongo testcontainer
}