package org.java.bot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserRepository {
    private Map<Long, List<String>> users;

    public UserService() {
        this.users = new HashMap<>();
    }

    @Override
    public void save(Long id) {
        log.info("Adding user: {}", id);
        users.put(id, new ArrayList<>());
    }

    @Override
    public Boolean exists(Long id) {
        boolean exists = users.containsKey(id);
        log.info("User with id {} exists: {}", id, exists);
        return exists;
    }

    @Override
    public Map<Long, List<String>> get() {
        log.info("Retrieving all users and their links");
        return users;
    }
}
