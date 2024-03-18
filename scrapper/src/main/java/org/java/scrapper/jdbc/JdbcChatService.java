package org.java.scrapper.jdbc;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.dto.chat.ChatRequest;
import org.java.scrapper.dto.chat.ChatResponse;
import org.java.scrapper.exception.ConflictException;
import org.java.scrapper.exception.NotFoundException;
import org.java.scrapper.mapper.ChatResponseMapper;
import org.java.scrapper.repository.ChatRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatRepository {
    private final static String SQL_SAVE = "INSERT INTO chats (chat_id, name, created_by) VALUES (?, ?, ?)";

    private final static String SQL_EXISTS = "SELECT EXISTS(SELECT * FROM chats WHERE chat_id = ?)";

    private final static String SQL_DELETE = "DELETE FROM chats WHERE chat_id = ?";

    private final static String SQL_GET = "SELECT * FROM chats WHERE chat_id = ?";

    private final static String SQL_GETS = "SELECT * FROM chats";

    private final static String CONFLICT_MESSAGE = "Chat already registered.";

    private final static String NOT_FOUND_MESSAGE = "Chat not found.";

    private final JdbcTemplate jdbcTemplate;
    private  final ChatResponseMapper mapper;

    @Override
    public void save(ChatRequest request) {
        if (exists(request.getChatId())) {
            throw new ConflictException(CONFLICT_MESSAGE);
        }

        log.info("Registering chat with ID: {} and name: {}", request.getChatId(), request.getName());
        jdbcTemplate.update(SQL_SAVE, request.getChatId(), request.getName(), request.getCreatedBy());
        log.info("Chat registered successfully!");
    }

    @Override
    public void delete(Long chatId) {
        if (!exists(chatId)) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        log.info("Deleting chat with ID: {}", chatId);
        jdbcTemplate.update(SQL_DELETE, chatId);
        log.info("Chat deleted successfully!");
    }

    @Override
    public Optional<ChatResponse> get(Long chatId) {
        if (!exists(chatId)) {
            return Optional.empty();
        }

        log.info("Retrieving chat with ID: {}", chatId);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET, mapper, chatId));
    }

    @Override
    public List<ChatResponse> gets() {
        log.info("Retrieving all chats");
        return jdbcTemplate.query(SQL_GETS, mapper);
    }

    private Boolean exists(Long chatId) {
        log.debug("Checking if chat with ID {} exists", chatId);
        return jdbcTemplate.queryForObject(SQL_EXISTS, Boolean.class, chatId);
    }
}
