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
    public Boolean isExists(Long id) {
        boolean exists = users.containsKey(id);
        log.info("User with id {} exists: {}", id, exists);
        return exists;
    }

    @Override
    public List<String> links(Long id) {
        List<String> userLinks = users.get(id);
        log.info("Retrieving links for user with id {}: {}", id, userLinks);
        return userLinks;
    }

    public Map<Long, List<String>> get() {
        log.info("Retrieving all users and their links");
        return users;
    }
}
