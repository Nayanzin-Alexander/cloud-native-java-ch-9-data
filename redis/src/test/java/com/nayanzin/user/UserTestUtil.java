package com.nayanzin.user;

import java.util.UUID;

abstract class UserTestUtil {

    static User getUser(String firstName, String lastName) {
        return User.builder()
                .uuid(UUID.randomUUID())
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
