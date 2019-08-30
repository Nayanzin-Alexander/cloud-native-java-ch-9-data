package com.nayanzin.user;

import com.nayanzin.UserServiceTestConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.nayanzin.user.UserTestUtil.getUser;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class UserServiceTest extends UserServiceTestConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void saveUserTest() {
        // When save user.
        User user = userService.saveUser(getUser("First name", "Last name"));

        // User exists in user repository.
        assertThat(userRepository.existsById(user.getId()), is(true));
    }

    @Test
    public void getUserTest() {
        // Given saved user.
        long userId = userService.saveUser(getUser("First name", "Last name")).getId();

        // When get user by id.
        User user = userService.getUser(userId);

        // Then user returns.
        assertThat(user, notNullValue());
    }
}