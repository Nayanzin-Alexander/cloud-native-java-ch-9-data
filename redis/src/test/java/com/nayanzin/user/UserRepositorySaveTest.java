package com.nayanzin.user;

import com.nayanzin.UserServiceTestConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.nayanzin.user.UserTestUtil.getUser;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class UserRepositorySaveTest extends UserServiceTestConfig {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveTest() {
        // Given user to save.
        User user = getUser("First Name", "Last Name");

        // When save user.
        user = userRepository.save(user);

        // Then user is available for retrieving from the repository.
        User savedUser =  userRepository.findById(user.getId())
                .orElse(null);
        assertThat(savedUser, notNullValue());
        assertThat(savedUser.getId(), greaterThanOrEqualTo(1L));
        assertThat(savedUser.getUuid(), notNullValue());
        assertThat(savedUser.getFirstName(), is("First Name"));
        assertThat(savedUser.getLastName(), is("Last Name"));
    }
}