package org.java.bot.repository;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    void save(Long id);

    Boolean exists(Long id);

    Map<Long, List<String>> get();
}
