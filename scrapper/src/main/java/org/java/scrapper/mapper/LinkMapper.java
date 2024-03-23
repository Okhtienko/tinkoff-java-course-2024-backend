package org.java.scrapper.mapper;

import java.net.URI;
import java.sql.ResultSet;
import java.time.ZoneOffset;
import lombok.SneakyThrows;
import org.java.scrapper.dto.link.LinkResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkResponseMapper implements RowMapper<LinkResponse> {

    @Override
    @SneakyThrows
    public LinkResponse mapRow(ResultSet resultSet, int row) {
        LinkResponse link = new LinkResponse().setId(resultSet.getLong("id"))
            .setUrl(new URI(resultSet.getString("url")))
            .setCreatedBy(resultSet.getString("create_by"))
            .setLastCheck(resultSet.getTimestamp("last_check").toLocalDateTime().atOffset(ZoneOffset.UTC))
            .setCreatedAt(resultSet.getTimestamp("create_at").toLocalDateTime().atOffset(ZoneOffset.UTC));
        return link;
    }
}
