package org.java.scrapper.mapper;

import java.sql.ResultSet;
import java.time.ZoneOffset;
import lombok.SneakyThrows;
import org.java.scrapper.model.Link;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper implements RowMapper<Link> {

    @Override
    @SneakyThrows
    public Link mapRow(ResultSet resultSet, int row) {
        Link link = Link.builder()
            .id(resultSet.getLong("id"))
            .url(resultSet.getString("url"))
            .createdBy(resultSet.getString("create_by"))
            .lastCheck(resultSet.getTimestamp("last_check").toLocalDateTime().atOffset(ZoneOffset.UTC))
            .createdAt(resultSet.getTimestamp("create_at").toLocalDateTime().atOffset(ZoneOffset.UTC))
            .build();
        return link;
    }
}
