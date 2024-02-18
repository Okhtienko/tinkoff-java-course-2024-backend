package org.java.bot.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.repository.LinkRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService implements LinkRepository {
    private final UserService userService;

    @Override
    public void save(String link, Long id) {
        userService.get().computeIfPresent(id, (key, links) -> {
            links.add(link);
            log.info("Link {} saved for user {}", link, id);
            return links;
        });
    }

    @Override
    public void remove(String link, Long id) {
        userService.get().computeIfPresent(id, (key, links) -> {
            links.remove(link);
            log.info("Link {} removed for user {}", link, id);
            return links;
        });
    }

    @Override
    public Boolean isBasicLinkFormat(String link) {
        String regex = "^(https?://)?(www\\.)?([a-zA-Z0-9]+[-a-zA-Z0-9]*\\.)+[a-zA-Z]{2,6}([/\\?].*)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(link);
        boolean isValid = matcher.matches();
        log.info("Link validation result for {}: {}", link, isValid);
        return isValid;
    }

    @Override
    public Boolean isExists(String link, Long id) {
        boolean exists = userService.links(id).contains(link);
        log.info("Link {} existence for user {}: {}", link, id, exists);
        return exists;
    }
}
