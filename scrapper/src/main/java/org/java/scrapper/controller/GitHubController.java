package org.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import org.java.scrapper.client.GitHubClient;
import org.java.scrapper.dto.github.GitHubCommitResponse;
import org.java.scrapper.dto.github.GitHubIssueResponse;
import org.java.scrapper.dto.github.GitHubPullResponse;
import org.java.scrapper.dto.github.GitHubRepositoryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/scrapper/github")
@RequiredArgsConstructor
public class GitHubController {
    private final GitHubClient githubClient;

    @GetMapping("/repository/{owner}/{repository}")
    public Mono<GitHubRepositoryResponse> fetchRepository(@PathVariable String owner, @PathVariable String repository) {
        return githubClient.fetchRepository(owner, repository);
    }

    @GetMapping("/commits/{owner}/{repository}")
    public Flux<GitHubCommitResponse> fetchCommits(@PathVariable String owner, @PathVariable String repository) {
        return githubClient.fetchCommits(owner, repository);
    }

    @GetMapping("/pulls/{owner}/{repository}")
    public Flux<GitHubPullResponse> fetchPulls(@PathVariable String owner, @PathVariable String repository) {
        return githubClient.fetchPulls(owner, repository);
    }

    @GetMapping("/issues/{owner}/{repository}")
    public Flux<GitHubIssueResponse> fetchIssues(@PathVariable String owner, @PathVariable String repository) {
        return githubClient.fetchIssues(owner, repository);
    }
}
