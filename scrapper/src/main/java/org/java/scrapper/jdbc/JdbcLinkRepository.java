package org.java.scrapper.jdbc;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.mapper.LinkMapper;
import org.java.scrapper.model.Link;
import org.java.scrapper.repository.LinkRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LinkMapper mapper;

    private final static String SQL_REMOVE = "DELETE FROM links WHERE url = ?";

    private final static String SQL_GET_LINK = "SELECT * FROM links WHERE url = ?";

    private final static String SQL_GET =
        """
        SELECT * FROM links INNER JOIN links_chats ON links.id = links_chats.link_id
        WHERE links_chats.chat_id = ? AND links.url = ?
        """;

    private final static String SQL_SAVE =
        """
        INSERT INTO links (url, create_by, last_check, create_at) VALUES (?, ?, ?, ?)
        """;

    private static final String SQL_GET_CHATS_BY_LAST_CHECK =
        """
        SELECT chat_id FROM links_chats INNER JOIN links ON links_chats.link_id = links.id
        WHERE links.url = ? AND links.last_check < ?
        ORDER BY links.last_check DESC LIMIT 100
        """;

    private static final String SQL_UPDATE = "UPDATE links SET last_check = ? WHERE id = ?";

    private final static String SQL_GETS_BY_LAST_CHECK =
        "SELECT * FROM links WHERE links.last_check < ? ORDER BY links.last_check DESC LIMIT 100";

    private final static String SQL_EXISTS_BY_URL = "SELECT EXISTS(SELECT * FROM links WHERE url = ?)";

    private final static String SQL_SAVE_LINKS_CHATS = "INSERT INTO links_chats (link_id, chat_id) VALUES (?, ?)";

    private final static String SQL_GETS =
        """
        SELECT * FROM links INNER JOIN links_chats ON links.id = links_chats.link_id WHERE links_chats.chat_id = ?
        """;

    private final static String SQL_EXISTS_LINKS_CHATS = "SELECT EXISTS(SELECT * FROM links_chats WHERE link_id = ?)";

    private final static String SQL_REMOVE_LINKS_CHATS = "DELETE FROM links_chats WHERE link_id = ? AND chat_id = ?";

    private final static String SQL_EXISTS_BY_URL_CHAT_ID =
        """
        SELECT EXISTS(SELECT * FROM links WHERE id IN (SELECT link_id FROM links_chats WHERE chat_id = ?) AND url = ?)
        """;

    @Override
    public Link save(String url, String createdBy, Long chatId) {
        log.info("Saving link: {}", url);
        jdbcTemplate.update(SQL_SAVE, url, createdBy, OffsetDateTime.now(), OffsetDateTime.now());

        Link link = get(url);
        updateLinkChat(link.getId(), chatId);
        log.info("Link saved with id: {}", link.getId());
        return link;
    }

    @Override
    public Link get(String url) {
        log.info("Getting link: {}", url);
        return jdbcTemplate.queryForObject(SQL_GET_LINK, mapper, url);
    }

    @Override
    public Link get(String url, Long chatId) {
        log.info("Getting link: {} for chat {}", url, chatId);
        return jdbcTemplate.queryForObject(SQL_GET, mapper, chatId, url);
    }

    @Override
    public Link remove(String url) {
        log.info("Removing link: {}", url);
        Link link = jdbcTemplate.queryForObject(SQL_GET_LINK, mapper, url);
        jdbcTemplate.update(SQL_REMOVE, url);
        log.info("Link removed: {}", url);
        return link;
    }

    @Override
    public List<Link> gets(Long chatId) {
        log.info("Getting all links for chat {}", chatId);
        return jdbcTemplate.query(SQL_GETS, mapper, chatId);
    }

    @Override
    public List<Link> getsByLastCheck(Long delay) {
        log.info("Getting links with last check before: {}", OffsetDateTime.now().minusSeconds(delay));
        return jdbcTemplate.query(SQL_GETS_BY_LAST_CHECK, mapper, OffsetDateTime.now().minusSeconds(delay));
    }

    @Override
    public List<Long> getsChatByLastCheck(Long delay, String url) {
        log.info("Getting chats for link {} with last check before: {}", url, OffsetDateTime.now().minusSeconds(delay));
        return jdbcTemplate.query(
            SQL_GET_CHATS_BY_LAST_CHECK,
            (rs, rowNum) -> rs.getLong("chat_id"),
            url,
            OffsetDateTime.now().minusSeconds(delay)
        );
    }

    @Override
    public Boolean existsByUrl(String url) {
        log.info("Checking existence of link: {}", url);
        return jdbcTemplate.queryForObject(SQL_EXISTS_BY_URL, Boolean.class, url);
    }

    @Override
    public Boolean existsByUrlAndChatId(String url, Long chatId) {
        log.info("Checking existence of link: {} for chat {}", url, chatId);
        return jdbcTemplate.queryForObject(SQL_EXISTS_BY_URL_CHAT_ID, Boolean.class, chatId, url);
    }

    @Override
    public Boolean existsLinkChat(Long id) {
        log.info("Checking existence of link chat with id: {}", id);
        return jdbcTemplate.queryForObject(SQL_EXISTS_LINKS_CHATS, Boolean.class, id);
    }

    @Override
    public void updateLinkChat(Long id, Long chatId) {
        log.info("Updating link chat: id={}, chatId={}", id, chatId);
        jdbcTemplate.update(SQL_SAVE_LINKS_CHATS, id, chatId);
    }

    @Override
    public void removeLinkChat(Long id, Long chatId) {
        log.info("Removing link chat: id={}, chatId={}", id, chatId);
        jdbcTemplate.update(SQL_REMOVE_LINKS_CHATS, id, chatId);
    }

    @Override
    public void updateLastCheck(Long id, OffsetDateTime date) {
        log.info("Updating last check for link: id={}, date={}", id, date);
        jdbcTemplate.update(SQL_UPDATE, Timestamp.from(date.toInstant()), id);
    }
}
