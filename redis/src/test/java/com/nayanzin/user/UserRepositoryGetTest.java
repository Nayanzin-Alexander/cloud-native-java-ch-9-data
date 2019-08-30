package com.nayanzin.user;

import com.nayanzin.UserServiceTestConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class UserRepositoryGetTest extends UserServiceTestConfig {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByIdTest() {
        // Given saved in test migration ${afterMigrate.sql} user.
        UUID uuid = UUID.fromString("a5fe6771-4e28-4438-99b9-7961ba8e072a");

        // When get user by id.
        User user = userRepository.findTopByUuid(uuid).orElse(null);

        // Then user returns.
        assertThat(user, notNullValue());
        assertThat(user.getId(), greaterThanOrEqualTo(1L));
        assertThat(user.getUuid(), is(uuid));
        assertThat(user.getFirstName(), is("First name"));
        assertThat(user.getLastName(), is("Last name"));
    }
}