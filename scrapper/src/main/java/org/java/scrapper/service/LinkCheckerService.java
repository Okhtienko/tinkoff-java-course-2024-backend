package org.java.scrapper.service;

import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.client.GitHubClient;
import org.java.scrapper.client.StackOverflowClient;
import org.java.scrapper.datamanager.LinkChecker;
import org.java.scrapper.model.Link;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class
LinkCheckerService implements LinkChecker {
    private static final String GIT_HUB_TYPE = "https://github.com/";
    private static final Integer GIT_HUB_OWNER = 3;
    private static final Integer GIT_HUB_REPOSITORY = 4;

    private static final Integer STACK_OVERFLOW_ID = 4;

    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Override
    public Boolean check(Link link) {
        log.info("Checking for updates on link: {}", link.getUrl());
        if (link.getUrl().startsWith(GIT_HUB_TYPE)) {
            return checkGitHubLink(link.getUrl(), link.getLastCheck());
        } else {
            return checkStackOverflowLink(link.getUrl(), link.getLastCheck());
        }
    }

    private boolean checkGitHubLink(String url, OffsetDateTime lastCheck) {
        String[] parts = url.split("/");
        String owner = parts[GIT_HUB_OWNER];
        String repository = parts[GIT_HUB_REPOSITORY];

        return gitHubClient.fetchPulls(owner, repository)
            .collectList()
            .block()
            .stream()
            .anyMatch(pull -> pull.getUpdatedDate().isAfter(lastCheck) || pull.getCreatedDate().isAfter(lastCheck));
    }

    private boolean checkStackOverflowLink(String url, OffsetDateTime lastCheck) {
        Long id = Long.parseLong(url.split("/")[STACK_OVERFLOW_ID]);

        return stackOverflowClient.fetchAnswers(id)
            .collectList()
            .block()
            .stream()
            .flatMap(answer -> answer.getItems().stream()).anyMatch(item ->
                item.getCreationDate().isAfter(lastCheck));
    }
}
