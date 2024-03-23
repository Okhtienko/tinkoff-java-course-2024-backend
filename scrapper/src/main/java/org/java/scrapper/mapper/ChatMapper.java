package org.java.scrapper.mapper;

import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.java.scrapper.model.Chat;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper implements RowMapper<Chat> {
    @Override
    @SneakyThrows
    public Chat mapRow(ResultSet resultSet, int row) {
        Chat chat = Chat.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .createdBy(resultSet.getString("created_by"))
            .build();
        return chat;
    }
}
