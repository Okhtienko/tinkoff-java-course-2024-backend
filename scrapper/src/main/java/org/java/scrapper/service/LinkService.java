package org.java.scrapper.jdbc;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.converter.LinkConverter;
import org.java.scrapper.dto.link.LinkRequest;
import org.java.scrapper.dto.link.LinkResponse;
import org.java.scrapper.exception.ConflictException;
import org.java.scrapper.exception.NotFoundException;
import org.java.scrapper.model.Link;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JdbcLinkService {
    private final static String CONFLICT_MESSAGE = "The link has already been saved.";

    private final static String NOT_FOUND_MESSAGE = "Link not found.";

    private final JdbcLinkRepository jdbcLinkRepository;
    private final LinkConverter converter;

    public LinkResponse save(LinkRequest request, Long chatId) {
        if (jdbcLinkRepository.exists(request.getUrl())) {
            throw new ConflictException(CONFLICT_MESSAGE);
        }

        Link link = jdbcLinkRepository.save(request.getUrl(), request.getCreatedBy(), chatId);

        return converter.toDto(link);
    }

    public LinkResponse get(LinkRequest request, Long chatId) {
        if (!jdbcLinkRepository.exists(request.getUrl())) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        Link link = jdbcLinkRepository.get(request.getUrl(), chatId);

        return converter.toDto(link);
    }

    public List<LinkResponse> gets(Long chatId) {
        List<Link> links = jdbcLinkRepository.gets(chatId);
        return converter.toDto(links);
    }

    public LinkResponse remove(LinkRequest request, Long chatId) {
        if (!jdbcLinkRepository.exists(request.getUrl())) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        Link link = jdbcLinkRepository.remove(request.getUrl(), chatId);
        return converter.toDto(link);
    }

    public List<LinkResponse> getsByLastCheck(Long delay) {
        List<Link> links = jdbcLinkRepository.getsByLastCheck(delay);
        return converter.toDto(links);
    }
}
