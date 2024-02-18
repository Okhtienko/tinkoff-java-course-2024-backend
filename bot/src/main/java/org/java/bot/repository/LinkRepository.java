package org.java.bot.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository {
    void save(String link, Long id);

    void remove(String link, Long id);

    Boolean isExists(String link, Long id);

    Boolean isBasicLinkFormat(String link);
}
