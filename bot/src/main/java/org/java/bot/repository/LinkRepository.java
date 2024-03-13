package org.java.bot.repository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository {
    void save(Long id, String link);

    void remove(Long id, String link);

    Boolean exists(Long id, String link);

    Boolean check(String link);

    List<String> get(Long id);
}
