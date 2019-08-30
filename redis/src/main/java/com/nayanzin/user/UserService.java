package com.nayanzin.user;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LogManager.getLogger();

    private final UserRepository userRepository;

    @CacheEvict(value = "user", key = "#user.getId()")
    public User saveUser(User user) {
        user = userRepository.save(user);
        log.info("Created new user with id:{}.", user.getId());
        return user;
    }

    @Cacheable(value = "user")
    public User getUser(long id) {
        return userRepository.findById(id)
                .orElse(null);
    }
}
