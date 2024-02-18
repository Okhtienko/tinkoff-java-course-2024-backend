package org.java.bot.repository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    void save(Long id);

    Boolean isExists(Long id);

    List<String> links(Long id);
}
