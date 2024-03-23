package org.java.scrapper.converter;

import java.util.List;
import org.java.scrapper.dto.chat.ChatResponse;
import org.java.scrapper.model.Chat;
import org.mapstruct.Mapper;

@Mapper
public interface ChatConverter {
    ChatResponse toDto(Chat chat);

    List<ChatResponse> toDto(List<Chat> chats);
}
