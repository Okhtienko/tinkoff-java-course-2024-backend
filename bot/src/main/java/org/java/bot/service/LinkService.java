package org.java.bot.service;

import java.util.List;
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
    public void save(Long id, String link) {
        userService.get().computeIfPresent(id, (key, links) -> {
            links.add(link);
            log.info("Link {} saved for user with ID {}", link, id);
            return links;
        });
    }

    @Override
    public void remove(Long id, String link) {
        userService.get().computeIfPresent(id, (key, links) -> {
            links.remove(link);
            log.info("Link {} removed for user with ID {}", link, id);
            return links;
        });
    }

    @Override
    public Boolean checkLinkFormat(String link) {
        String regex = "^(https?://)?(www\\.)?([a-zA-Z0-9]+[-a-zA-Z0-9]*\\.)+[a-zA-Z]{2,6}([/\\?].*)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(link);
        boolean isValid = matcher.matches();
        log.info("Link {} format is valid: {}", link, isValid);
        return isValid;
    }

    @Override
    public List<String> get(Long id) {
        return userService.get().get(id);
    }

    @Override
    public Boolean exists(Long id, String link) {
        Boolean exists = get(id).contains(link);
        log.info("Link {} exists for user with ID {}: {}", link, id, exists);
        return exists;
    }
}
