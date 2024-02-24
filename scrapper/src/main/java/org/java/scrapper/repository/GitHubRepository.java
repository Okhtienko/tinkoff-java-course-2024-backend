package org.java.scrapper.repository;

import org.java.scrapper.dto.github.GitHubCommitResponse;
import org.java.scrapper.dto.github.GitHubIssueResponse;
import org.java.scrapper.dto.github.GitHubPullResponse;
import org.java.scrapper.dto.github.GitHubRepositoryResponse;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GitHubRepository  {
    Mono<GitHubRepositoryResponse> fetchRepository(String owner, String repository);

    Flux<GitHubCommitResponse> fetchCommits(String owner, String repository);

    Flux<GitHubIssueResponse> fetchIssues(String owner, String repository);

    Flux<GitHubPullResponse> fetchPulls(String owner, String repository);
}
