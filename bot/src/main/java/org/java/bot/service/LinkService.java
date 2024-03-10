package org.java.bot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.repository.LinkRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LinkService implements LinkRepository {
    private Map<Long, List<String>> links;

    public LinkService() {
        this.links = new HashMap<>();
    }

    @Override
    public void save(Long id, String link) {
        links.computeIfPresent(id, (key, links) -> {
            links.add(link);
            log.info("Link {} saved for user with ID {}", link, id);
            return links;
        });
    }

    @Override
    public void remove(Long id, String link) {
        links.computeIfPresent(id, (key, links) -> {
            links.remove(link);
            log.info("Link {} removed for user with ID {}", link, id);
            return links;
        });
    }

    @Override
    public Boolean checkLinkFormat(String link) {
        String regex = "^(https?://)?([a-zA-Z0-9]+[a-zA-Z0-9-]*\\.)+[a-zA-Z]{2,6}(/.*)?$";
        log.info("Link {} format is valid: {}", link, regex);
        return link.matches(regex);
    }

    @Override
    public List<String> get(Long id) {
        return links.get(id);
    }

    @Override
    public Boolean exists(Long id, String link) {
        Boolean exists = get(id).contains(link);
        log.info("Link {} exists for user with ID {}: {}", link, id, exists);
        return exists;
    }
}
