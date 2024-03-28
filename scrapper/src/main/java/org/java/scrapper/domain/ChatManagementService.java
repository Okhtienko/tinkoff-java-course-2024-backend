package org.java.scrapper.domain;

import java.util.List;
import org.java.scrapper.dto.chat.ChatRequest;
import org.java.scrapper.dto.chat.ChatResponse;

public interface ChatManagementService {
    void save(ChatRequest request);

    void delete(Long id);

    ChatResponse get(Long id);

    List<ChatResponse> gets();
}
