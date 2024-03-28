package org.java.scrapper.jdbc;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.mapper.ChatMapper;
import org.java.scrapper.model.Chat;
import org.java.scrapper.repository.ChatRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final ChatMapper mapper;
    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_GETS = "SELECT * FROM chats";

    private static final String SQL_GET = "SELECT * FROM chats WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM chats WHERE id = ?";

    private static final String SQL_EXISTS = "SELECT EXISTS(SELECT * FROM chats WHERE id = ?)";

    private static final String SQL_DELETE_LINK_CHAT = "DELETE FROM links_chats WHERE chat_id = ?";

    private static final String SQL_SAVE = "INSERT INTO chats (id, name, created_by) VALUES (?, ?, ?)";

    private static final String SQL_EXISTS_IN_LINK_CHAT = "SELECT EXISTS(SELECT * FROM links_chats WHERE chat_id = ?)";

    @Override
    public void save(Long id, String name, String createdBy) {
        log.info("Registering chat with ID: {} and name: {}", id, name);
        jdbcTemplate.update(SQL_SAVE, id, name, createdBy);
        log.info("Chat registered successfully!");
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting chat with ID: {}", id);

        if (existsInLinkChat(id)) {
            jdbcTemplate.update(SQL_DELETE_LINK_CHAT, id);
        }

        jdbcTemplate.update(SQL_DELETE, id);
        log.info("Chat deleted successfully!");
    }

    @Override
    public Chat get(Long id) {
        log.info("Retrieving chat with ID: {}", id);
        return jdbcTemplate.queryForObject(SQL_GET, mapper, id);
    }

    @Override
    public List<Chat> gets() {
        log.info("Retrieving all chats");
        return jdbcTemplate.query(SQL_GETS, mapper);
    }

    @Override
    public Boolean exists(Long id) {
        log.info("Checking if chat with ID {} exists", id);
        return jdbcTemplate.queryForObject(SQL_EXISTS, Boolean.class, id);
    }

    public Boolean existsInLinkChat(Long id) {
        log.info("Checking if chat with ID {} exists in link_chats table", id);
        return jdbcTemplate.queryForObject(SQL_EXISTS_IN_LINK_CHAT, Boolean.class, id);
    }
}
