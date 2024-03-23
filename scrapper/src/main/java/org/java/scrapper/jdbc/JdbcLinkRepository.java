package org.java.scrapper.service;

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
    private final static String SQL_SAVE =
        "INSERT INTO links (url, create_by, last_check, create_at) VALUES (?, ?, ?, ?)";
    private final static String SQL_GETS =
        "SELECT * FROM links INNER JOIN links_chats ON links.id = links_chats.link_id WHERE links_chats.chat_id = ?";

    private final static String SQL_GET =
        "SELECT * FROM links INNER JOIN links_chats ON links.id = links_chats.link_id "
            + "WHERE links_chats.chat_id = ? AND links.url = ?";

    private final static String SQL_REMOVE =
        "DELETE FROM links WHERE id IN (SELECT link_id FROM links_chats WHERE chat_id = ?) AND url = ?";

    private final static String SQL_EXISTS = "SELECT EXISTS(SELECT * FROM links WHERE url = ?)";

    private final static String SQL_GET_LINK = "SELECT * FROM links WHERE url = ?";

    private final static String SQL_SAVE_LINKS_CHATS = "INSERT INTO links_chats (link_id, chat_id) VALUES (?, ?)";

    private final static String SQL_GETS_BY_LAST_CHECK = "SELECT * FROM links WHERE links.last_check < ?";

    private static final String SQL_GET_CHATS_BY_LAST_CHECK =
        "SELECT chat_id FROM links_chats INNER JOIN links ON links_chats.link_id = links.id "
            + "WHERE links.url = ? AND links.last_check < ?";

    private static final String SQL_UPDATE = "UPDATE links SET last_check = ? WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final LinkMapper mapper;

    @Override
    public Link save(String url, String createdBy, Long chatId) {
        log.info("Saving link: {}", url);
        jdbcTemplate.update(SQL_SAVE, url, createdBy, OffsetDateTime.now(), OffsetDateTime.now());

        Link link = jdbcTemplate.queryForObject(SQL_GET_LINK, mapper, url);

        jdbcTemplate.update(SQL_SAVE_LINKS_CHATS, link.getId(), chatId);
        log.info("Link saved with id: {}", link.getId());

        return link;
    }

    @Override
    public Link get(String url, Long chatId) {
        log.info("Getting link: {} for chat {}", url, chatId);
        return jdbcTemplate.queryForObject(SQL_GET, mapper, chatId, url);
    }

    @Override
    public Link remove(String url, Long chatId) {
        Link link = jdbcTemplate.queryForObject(SQL_GET_LINK, mapper, url);

        jdbcTemplate.update(SQL_REMOVE, chatId, url);

        log.info("Link removed: {}", url);
        return link;
    }

    @Override
    public List<Link> gets(Long chatId) {
        log.info("Getting all links for chat {}", chatId);
        return jdbcTemplate.query(SQL_GETS, mapper, chatId);
    }

    @Override
    public List<Link> getsByLastCheck() {
        return jdbcTemplate.query(SQL_GETS_BY_LAST_CHECK, mapper, OffsetDateTime.now().minusHours(24));
    }

    @Override
    public List<Long> getsChatByLastCheck(String url) {
        return jdbcTemplate.query(
            SQL_GET_CHATS_BY_LAST_CHECK,
            (rs, rowNum) -> rs.getLong("chat_id"),
            url,
            OffsetDateTime.now().minusHours(24)
        );
    }

    @Override
    public Boolean exists(String url) {
        log.info("Checking if link exists: {}", url);
        return jdbcTemplate.queryForObject(SQL_EXISTS, Boolean.class, url);
    }

    @Override
    public void update(Long id) {
        jdbcTemplate.update(SQL_UPDATE, Timestamp.from(OffsetDateTime.now().toInstant()), id);
    }
}
