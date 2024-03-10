package org.java.bot.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    void save(Long id);

    Boolean exists(Long id);
}
