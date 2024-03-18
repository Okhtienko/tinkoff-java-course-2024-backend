package org.java.scrapper.repository;

import java.util.List;
import java.util.Optional;
import org.java.scrapper.dto.chat.ChatRequest;
import org.java.scrapper.dto.chat.ChatResponse;

public interface ChatRepository {
    void save(ChatRequest request);

    void delete(Long chatId);

    Optional<ChatResponse> get(Long chatId);

    List<ChatResponse> gets();
}
