package org.java.scrapper.scheduler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.client.BotClient;
import org.java.scrapper.dto.link.LinkUpdateRequest;
import org.java.scrapper.jdbc.JdbcLinkRepository;
import org.java.scrapper.model.Link;
import org.java.scrapper.service.LinkCheckerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.scheduler", name = "enable", havingValue = "true")
public class LinkUpdaterScheduler {
    private final BotClient botClient;
    private final JdbcLinkRepository jdbcLinkRepository;
    private final LinkCheckerService linkCheckerService;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        log.info("Placeholder log for update method.");
        List<Link> links = jdbcLinkRepository.getsByLastCheck();
        for (Link link : links) {
            if (linkCheckerService.check(link)) {
                List<Long> chats = jdbcLinkRepository.getsChatByLastCheck(link.getUrl());
                LinkUpdateRequest request = build(link, chats);
                botClient.sendUpdate(request)
                    .subscribe(response -> log.info("Update notification sent for link: {}", link.getUrl()));
                jdbcLinkRepository.update(link.getId());
            }
        }
    }

    private LinkUpdateRequest build(Link link, List<Long> chats) {
        return new LinkUpdateRequest().setId(link.getId())
            .setUrl(link.getUrl())
            .setDescription("Have updates.")
            .setChats(chats);
    }
}
