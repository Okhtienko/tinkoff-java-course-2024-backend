package org.java.scrapper.repository;

import java.time.OffsetDateTime;
import java.util.List;
import org.java.scrapper.model.Link;

public interface LinkRepository {
    Link get(String url);

    Link remove(String url);

    Link get(String url, Long chatId);

    Link save(String url, String createdBy, Long chatId);

    List<Link> gets(Long chatId);

    List<Link> getsByLastCheck(Long delay);

    List<Long> getsChatByLastCheck(Long delay, String url);

    Boolean existsByUrl(String url);

    Boolean existsLinkChat(Long id);

    Boolean existsByUrlAndChatId(String url, Long chatId);

    void updateLinkChat(Long id, Long chatId);

    void removeLinkChat(Long id, Long chatId);

    void updateLastCheck(Long id, OffsetDateTime date);
}
