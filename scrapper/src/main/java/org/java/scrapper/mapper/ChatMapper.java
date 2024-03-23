package org.java.scrapper.mapper;

import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.java.scrapper.dto.chat.ChatResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatResponseMapper implements RowMapper<ChatResponse> {
    @Override
    @SneakyThrows
    public ChatResponse mapRow(ResultSet resultSet, int row) {
        ChatResponse chat = new ChatResponse().setId(resultSet.getLong("id"))
            .setChatId(resultSet.getLong("chat_id"))
            .setName(resultSet.getString("name"))
            .setCreatedBy(resultSet.getString("created_by"));
        return chat;
    }
}
