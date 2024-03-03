package org.java.scrapper.client;

import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.datamanager.GitHubDataManager;
import org.java.scrapper.dto.github.GitHubCommitResponse;
import org.java.scrapper.dto.github.GitHubIssueResponse;
import org.java.scrapper.dto.github.GitHubPullResponse;
import org.java.scrapper.dto.github.GitHubRepositoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GitHubClient implements GitHubDataManager {
    private static final String DEFAULT_GITHUB_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClient(@Value("${client.github-base-url}") String githubBaseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(githubBaseUrl == null || githubBaseUrl.isEmpty()
                ? DEFAULT_GITHUB_URL
                : githubBaseUrl)
            .build();
    }

    @Override
    public Mono<GitHubRepositoryResponse> fetchRepository(String owner, String repository) {
        log.info("Fetching repository {} for owner {}", repository, owner);
        return webClient.get()
            .uri("/repos/{owner}/{repository}", owner, repository)
            .retrieve()
            .bodyToMono(GitHubRepositoryResponse.class);
    }

    @Override
    public Flux<GitHubCommitResponse> fetchCommits(String owner, String repository) {
        log.info("Fetching commits for repository {} owned by {}", repository, owner);
        return webClient.get()
            .uri("/repos/{owner}/{repository}/commits", owner, repository)
            .retrieve()
            .bodyToFlux(GitHubCommitResponse.class);
    }

    @Override
    public Flux<GitHubIssueResponse> fetchIssues(String owner, String repository) {
        log.info("Fetching issues for repository {} owned by {}", repository, owner);
        return webClient.get()
            .uri("/repos/{owner}/{repository}/issues", owner, repository)
            .retrieve()
            .bodyToFlux(GitHubIssueResponse.class);
    }

    @Override
    public Flux<GitHubPullResponse> fetchPulls(String owner, String repository) {
        log.info("Fetching pull requests for repository {} owned by {}", repository, owner);
        return webClient.get()
            .uri("/repos/{owner}/{repository}/pulls", owner, repository)
            .retrieve()
            .bodyToFlux(GitHubPullResponse.class);
    }
}
