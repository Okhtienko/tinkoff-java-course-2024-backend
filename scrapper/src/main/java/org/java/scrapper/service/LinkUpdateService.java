package org.java.scrapper.service;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.client.BotClient;
import org.java.scrapper.domain.LinkManagementService;
import org.java.scrapper.dto.link.LinkResponse;
import org.java.scrapper.dto.link.LinkUpdateRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkUpdateService {
    private final BotClient botClient;
    private final LinkCheckerService linkCheckerService;
    private final LinkManagementService linkManagementService;

    public void processUpdate(Long delay) {
        List<LinkResponse> links = linkManagementService.getsByLastCheck(delay);
        links.stream()
            .filter(linkCheckerService::check)
            .forEach(link -> handleLinkUpdate(link, delay));
    }

    private void handleLinkUpdate(LinkResponse link, Long delay) {
        List<Long> chats = linkManagementService.getsChatByLastCheck(delay, link);
        LinkUpdateRequest request = build(link, chats);
        botClient.sendUpdate(request)
            .subscribe(response -> log.info("Update notification sent for link: {}", link.getUrl()));
        linkManagementService.updateLastCheck(link, OffsetDateTime.now());
    }

    private LinkUpdateRequest build(LinkResponse link, List<Long> chats) {
        return new LinkUpdateRequest().setId(link.getId())
            .setUrl(link.getUrl().toString())
            .setDescription("Have updates.")
            .setChats(chats);
    }
}
