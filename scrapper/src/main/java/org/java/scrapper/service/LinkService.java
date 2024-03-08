package org.java.scrapper.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.dto.LinkResponse;
import org.java.scrapper.exception.BadRequestException;
import org.java.scrapper.exception.ConflictException;
import org.java.scrapper.exception.NotFoundException;
import org.java.scrapper.repository.LinkRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LinkService implements LinkRepository {
    private Map<Long, List<LinkResponse>> links;

    public LinkService() {
        this.links = new HashMap<>();
    }

    @Override
    public void register(Long id) {
        validateChat(id);
        if (links.containsKey(id)) {
            throw new ConflictException("Chat already registered");
        }
        links.put(id, new ArrayList<>());
    }

    @Override
    public void delete(Long id) {
        validateChat(id);
        if (!links.containsKey(id)) {
            throw new NotFoundException("Chat not found");
        }
        links.remove(id);
    }

    @Override
    public List<LinkResponse> gets(Long id) {
        log.info("Retrieving links for chat ID: {}", id);
        if (!links.containsKey(id)) {
            throw new BadRequestException("Invalid Chat ID");
        }
        return links.get(id);
    }

    @Override
    public LinkResponse save(Long id, String url) throws URISyntaxException {
        log.info("Saving link for chat ID: {} with URL: {}", id, url);
        validateUrl(url);
        LinkResponse link = new LinkResponse().setId(1L).setUrl(new URI(url));

        links.computeIfPresent(id, (key, links) -> {
            links.add(link);
            return links;
        });

        return link;
    }

    @Override
    public LinkResponse remove(Long id, String url) throws URISyntaxException {
        log.info("Removing link for chat ID: {} with URL: {}", id, url);
        validateUrl(url);

        if (!exists(id, url)) {
            throw new NotFoundException("Link not found");
        }

        LinkResponse link = new LinkResponse().setId(1L).setUrl(new URI(url));

        links.computeIfPresent(id, (key, links) -> {
            links.add(link);
            return links;
        });

        return link;
    }

    private void validateChat(Long id) {
        if (id == null) {
            throw new BadRequestException("Invalid request parameters: Chat ID cannot be null");
        }
    }

    private void validateUrl(String url) {
        if (!checkLinkFormat(url)) {
            throw new BadRequestException("Invalid request parameters");
        }
    }

    private Boolean checkLinkFormat(String url) {
        String regex = "^(https?://)?([a-zA-Z0-9]+[a-zA-Z0-9-]*\\.)+[a-zA-Z]{2,6}(/.*)?$";
        return url.matches(regex);
    }

    private Boolean exists(Long id, String url) {
        return links.get(id).stream().anyMatch(link -> {
            try {
                return link.getUrl().equals(new URI(url));
            } catch (URISyntaxException e) {
                return false;
            }
        });
    }
}
