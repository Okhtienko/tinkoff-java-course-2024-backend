package org.java.scrapper.service;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.converter.LinkConverter;
import org.java.scrapper.domain.LinkManagementService;
import org.java.scrapper.dto.link.LinkRequest;
import org.java.scrapper.dto.link.LinkResponse;
import org.java.scrapper.exception.ConflictException;
import org.java.scrapper.exception.NotFoundException;
import org.java.scrapper.model.Link;
import org.java.scrapper.repository.LinkRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService implements LinkManagementService {
    private final static String CONFLICT_MESSAGE = "The link has already been saved.";
    private final static String NOT_FOUND_MESSAGE = "Link not found.";

    private final LinkRepository linkRepository;
    private final LinkConverter converter;

    @Override
    public LinkResponse save(LinkRequest request, Long chatId) {
        if (linkRepository.existsByUrlAndChatId(request.getUrl(), chatId)) {
            throw new ConflictException(CONFLICT_MESSAGE);
        }

        Link link = linkRepository.existsByUrl(request.getUrl())
            ? updateLinkChat(request.getUrl(), chatId)
            : linkRepository.save(request.getUrl(), request.getCreatedBy(), chatId);

        return converter.toDto(link);
    }

    @Override
    public LinkResponse get(LinkRequest request, Long chatId) {
        if (!linkRepository.existsByUrlAndChatId(request.getUrl(), chatId)) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        Link link = linkRepository.get(request.getUrl(), chatId);

        return converter.toDto(link);
    }

    @Override
    public List<LinkResponse> gets(Long chatId) {
        List<Link> links = linkRepository.gets(chatId);
        return converter.toDto(links);
    }

    @Override
    public LinkResponse remove(LinkRequest request, Long chatId) {
        if (!linkRepository.existsByUrlAndChatId(request.getUrl(), chatId)) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        Link link = linkRepository.get(request.getUrl(), chatId);

        linkRepository.removeLinkChat(link.getId(), chatId);

        if (!linkRepository.existsLinkChat(link.getId())) {
            linkRepository.remove(request.getUrl());
        }

        return converter.toDto(link);
    }

    @Override
    public List<LinkResponse> getsByLastCheck(Long delay) {
        List<Link> links = linkRepository.getsByLastCheck(delay);
        return converter.toDto(links);
    }

    @Override
    public List<Long> getsChatByLastCheck(Long delay, LinkResponse link) {
        if (!linkRepository.existsByUrl(link.getUrl().toString())) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        return linkRepository.getsChatByLastCheck(delay, link.getUrl().toString());
    }

    @Override
    public void updateLastCheck(LinkResponse response, OffsetDateTime date) {
        linkRepository.updateLastCheck(response.getId(), date);
    }

    private Link updateLinkChat(String url, Long chatId) {
        Link link = linkRepository.get(url);
        linkRepository.updateLinkChat(link.getId(), chatId);
        return link;
    }
}
