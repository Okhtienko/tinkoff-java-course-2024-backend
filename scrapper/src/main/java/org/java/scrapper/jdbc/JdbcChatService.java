package org.java.scrapper.jdbc;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.dto.chat.ChatRequest;
import org.java.scrapper.dto.chat.ChatResponse;
import org.java.scrapper.mapper.ChatResponseMapper;
import org.java.scrapper.repository.ChatRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatRepository {
    private final static String SQL_SAVE = "INSERT INTO chats (chat_id, name, created_by) VALUES (?, ?, ?)";
    private final static String SQL_DELETE = "DELETE FROM chats WHERE id = ?";
    private final static String SQL_GET = "SELECT * FROM chats WHERE id = ?";
    private final static String SQL_GETS = "SELECT * FROM chats";

    private final JdbcTemplate jdbcTemplate;
    private  final ChatResponseMapper chatMapper;

    @Override
    public void save(ChatRequest request) {
        log.info("Registering chat with ID: {} and name: {}", request.getChatId(), request.getName());
        jdbcTemplate.update(SQL_SAVE, request.getChatId(), request.getName(), request.getCreatedBy());
        log.info("Chat registered successfully!");
    }

    @Override
    public void delete(Long chatId) {
        log.info("Deleting chat with ID: {}", chatId);
        jdbcTemplate.update(SQL_DELETE, chatId);
        log.info("Chat deleted successfully!");
    }

    @Override
    public ChatResponse get(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET, chatMapper, id);
    }

    @Override
    public List<ChatResponse> gets() {
        List<ChatResponse> chats = jdbcTemplate.query(SQL_GETS, chatMapper);
        log.info("Retrieved {} chats", chats.size());
        return chats;
    }
}
