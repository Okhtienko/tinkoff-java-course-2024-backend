package org.java.scrapper.service;

import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.client.GitHubClient;
import org.java.scrapper.client.StackOverflowClient;
import org.java.scrapper.datamanager.LinkChecker;
import org.java.scrapper.dto.github.GitHubPullResponse;
import org.java.scrapper.dto.link.LinkResponse;
import org.java.scrapper.dto.stackoverflow.StackOverflowItemResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class
LinkCheckerService implements LinkChecker {
    private static final String GIT_HUB_HOST = "https://github.com";
    private static final String STACK_OVERFLOW_HOST = "https://stackoverflow.com";
    private static final Integer GIT_HUB_OWNER = 3;
    private static final Integer GIT_HUB_REPOSITORY = 4;

    private static final Integer STACK_OVERFLOW_ID = 4;

    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Override
    public Boolean check(LinkResponse link) {
        log.info("Checking for updates on link: {}", link.getUrl());
        String host = link.getUrl().getScheme() + "://" + link.getUrl().getHost();

        switch (host) {
            case GIT_HUB_HOST:
                return checkGitHubLink(link.getUrl().toString(), link.getLastCheck());
            case STACK_OVERFLOW_HOST:
                return checkStackOverflowLink(link.getUrl().toString(), link.getLastCheck());
            default:
                return false;
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
            .anyMatch(pull -> isPullAfterLastCheck(pull, lastCheck));
    }

    private boolean isPullAfterLastCheck(GitHubPullResponse pull, OffsetDateTime lastCheck) {
        return pull.getUpdatedDate().isAfter(lastCheck) || pull.getCreatedDate().isAfter(lastCheck);
    }

    private boolean checkStackOverflowLink(String url, OffsetDateTime lastCheck) {
        Long id = Long.parseLong(url.split("/")[STACK_OVERFLOW_ID]);

        return stackOverflowClient.fetchAnswers(id)
            .collectList()
            .block()
            .stream()
            .flatMap(answer -> answer.getItems().stream()).anyMatch(item -> isAnswerAfterLastCheck(item, lastCheck));
    }

    private boolean isAnswerAfterLastCheck(StackOverflowItemResponse item, OffsetDateTime lastCheck) {
        return item.getCreationDate().isAfter(lastCheck)
            || item.getLastActivityDate().isAfter(lastCheck)
            || (item.getLastEditDate() != null && item.getLastEditDate().isAfter(lastCheck));
    }
}
