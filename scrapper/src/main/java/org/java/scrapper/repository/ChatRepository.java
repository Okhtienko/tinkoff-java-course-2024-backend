package org.java.scrapper.repository;

import java.util.List;
import org.java.scrapper.dto.chat.ChatRequest;
import org.java.scrapper.dto.chat.ChatResponse;

public interface ChatRepository {
    void save(ChatRequest request);

    void delete(Long chatId);

    ChatResponse get(Long id);

    List<ChatResponse> gets();
}
