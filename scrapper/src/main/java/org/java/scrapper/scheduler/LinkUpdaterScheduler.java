package org.java.scrapper.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.service.LinkUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.scheduler", name = "enable", havingValue = "true")
public class LinkUpdaterScheduler {
    private final LinkUpdateService linkUpdateService;

    @Value("${app.scheduler.force-check-delay}")
    private Long delay;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        log.info("Starting link update process.");
        linkUpdateService.processUpdate(delay);
    }
}
