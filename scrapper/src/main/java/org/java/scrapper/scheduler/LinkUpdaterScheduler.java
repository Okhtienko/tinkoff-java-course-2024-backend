package org.java.scrapper.scheduler;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.configuration.ApplicationConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LinkUpdaterScheduler {
    private final Duration delay;

    public LinkUpdaterScheduler(ApplicationConfig config) {
        this.delay = config.scheduler().interval();
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        log.info("Placeholder log for update method.");
    }
}
