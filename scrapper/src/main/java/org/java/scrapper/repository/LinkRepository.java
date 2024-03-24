package org.java.scrapper.repository;

import java.util.List;
import org.java.scrapper.model.Link;

public interface LinkRepository {
    Link save(String url, String createdBy, Long chatId);

    Link get(String url, Long chatId);

    Link remove(String url, Long chatId);

    List<Link> gets(Long chatId);

    List<Link> getsByLastCheck();

    List<Long> getsChatByLastCheck(String url);

    Boolean exists(String url);

    void update(Long id);
}
