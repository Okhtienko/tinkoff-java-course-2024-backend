package org.java.bot.service;

import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserRepository {
    private Set<Long> users;

    public UserService() {
        this.users = new HashSet<>();
    }

    @Override
    public void save(Long id) {
        log.info("Adding user: {}", id);
        users.add(id);
    }

    @Override
    public Boolean exists(Long id) {
        boolean exists = users.contains(id);
        log.info("User with id {} exists: {}", id, exists);
        return exists;
    }
}
