package org.java.scrapper.repository;

import java.time.OffsetDateTime;
import java.util.List;
import org.java.scrapper.model.Link;

public interface LinkRepository {
    Link save(String url, String createdBy, Long chatId);

    Link get(String url, Long chatId);

    Link remove(String url, Long chatId);

    List<Link> gets(Long chatId);

    List<Link> getsByLastCheck(Long delay);

    List<Long> getsChatByLastCheck(Long delay, String url);

    Boolean exists(String url);

    void updateLastCheck(Long id, OffsetDateTime date);
}
