package org.java.scrapper.jdbc;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.dto.link.LinkRequest;
import org.java.scrapper.dto.link.LinkResponse;
import org.java.scrapper.mapper.LinkResponseMapper;
import org.java.scrapper.repository.LinkRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkRepository {
    private final static int INDEX_LAST_CHECK = 3;
    private final static int INDEX_CREATED_AT = 4;
    private final static String SQL_SAVE =
        "INSERT INTO links (url, create_by, last_check, create_at) VALUES (?, ?, ?, ?)";
    private final static String SQL_GETS =
        "SELECT * FROM links INNER JOIN links_chats ON links.id = links_chats.link_id WHERE links_chats.chat_id = ?";

    private final static String SQL_GET =
        "SELECT * FROM links INNER JOIN links_chats ON links.id = links_chats.link_id "
            + "WHERE links_chats.chat_id = ? AND links.url = ?";

    private final static String SQL_REMOVE =
        "DELETE FROM links WHERE id IN (SELECT link_id FROM links_chats WHERE chat_id = ?) AND url = ?";

    private final JdbcTemplate jdbcTemplate;
    private final LinkResponseMapper linkMapper;

    @Override
    public LinkResponse save(LinkRequest request, Long chatId) throws URISyntaxException {
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(
            connection -> {
                PreparedStatement statement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, request.getUrl());
                statement.setString(2, request.getCreatedBy());
                statement.setObject(INDEX_LAST_CHECK, OffsetDateTime.now());
                statement.setObject(INDEX_CREATED_AT, OffsetDateTime.now());
                return statement;
            },
            holder
        );

        Long id =  (Long) holder.getKeys().get("id");

        jdbcTemplate.update("INSERT INTO links_chats (link_id, chat_id) VALUES (?, ?)", id, chatId);

        return build(id, request);
    }

    @Override
    public LinkResponse get(LinkRequest request, Long chatId) {
        return jdbcTemplate.queryForObject(SQL_GET, linkMapper, chatId, request.getUrl());
    }

    @Override
    public void remove(String url, Long chatId) {
        jdbcTemplate.update(SQL_REMOVE, chatId, url);
    }

    @Override
    public List<LinkResponse> gets(Long chatId) {
        return jdbcTemplate.query(SQL_GETS, linkMapper, chatId);
    }

    private LinkResponse build(Long id, LinkRequest request) throws URISyntaxException {
        return new LinkResponse().setId(id)
            .setUrl(new URI(request.getUrl()))
            .setCreatedBy(request.getCreatedBy())
            .setLastCheck(OffsetDateTime.now())
            .setCreatedAt(OffsetDateTime.now());
    }
}
