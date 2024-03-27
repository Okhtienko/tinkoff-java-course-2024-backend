package org.java.scrapper.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.converter.ChatConverter;
import org.java.scrapper.domain.ChatManagementService;
import org.java.scrapper.dto.chat.ChatRequest;
import org.java.scrapper.dto.chat.ChatResponse;
import org.java.scrapper.exception.ConflictException;
import org.java.scrapper.exception.NotFoundException;
import org.java.scrapper.jdbc.JdbcChatRepository;
import org.java.scrapper.model.Chat;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService implements ChatManagementService {
    private final static String CONFLICT_MESSAGE = "Chat already registered.";

    private final static String NOT_FOUND_MESSAGE = "Chat not found.";

    private final JdbcChatRepository jdbcChatRepository;
    private final ChatConverter converter;

    @Override
    public void save(ChatRequest request) {
     if (jdbcChatRepository.exists(request.getId())) {
         throw new ConflictException(CONFLICT_MESSAGE);
     }
     jdbcChatRepository.save(request.getId(), request.getName(), request.getCreatedBy());
    }

    @Override
    public void delete(Long id) {
        if (!jdbcChatRepository.exists(id)) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        jdbcChatRepository.delete(id);
    }

    @Override
    public ChatResponse get(Long id) {
        if (!jdbcChatRepository.exists(id)) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        Chat chat = jdbcChatRepository.get(id);
        return converter.toDto(chat);
    }

    @Override
    public List<ChatResponse> gets() {
        List<Chat> chats = jdbcChatRepository.gets();
        return converter.toDto(chats);
    }
}
