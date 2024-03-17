package org.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.java.scrapper.dto.github.GitHubCommitResponse;
import org.java.scrapper.dto.github.GitHubIssueResponse;
import org.java.scrapper.dto.github.GitHubPullResponse;
import org.java.scrapper.dto.github.GitHubRepositoryResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
@Disabled
public class GitHubClientTest {

    @Autowired
    private GitHubClient gitHubClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("github-base-url", wireMockExtension::baseUrl);
    }

    @Test
    public void testFetchRepositorySuccess() {
        wireMockExtension.stubFor(get("/repos/octocat/Hello-World")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile("github/repository.json")));

        Mono<GitHubRepositoryResponse> response = gitHubClient.fetchRepository(
            "octocat",
            "Hello-world"
        );

        StepVerifier.create(response)
            .expectNextMatches(repository -> repository.getName().equals("Hello-World"))
            .verifyComplete();
    }

    @Test
    public void testFetchRepositoryNotFound() {
        wireMockExtension.stubFor(get("/repos/unknown/unknown")
            .willReturn(aResponse()
                .withStatus(404)
                .withBody("Not Found")));

        Mono<GitHubRepositoryResponse> response = gitHubClient.fetchRepository(
            "unknown",
            "NonexistentRepo"
        );

        StepVerifier.create(response)
            .expectError(WebClientResponseException.NotFound.class)
            .verify();
    }

    @Test
    public void testFetchCommitsSuccess() {
        wireMockExtension.stubFor(get("/repos/octocat/Hello-World/commits")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile("github/commits.json")));

        Flux<GitHubCommitResponse> response = gitHubClient.fetchCommits("octocat", "Hello-World");
        List<GitHubCommitResponse> commits = response.collectList().block();

        assertEquals(3, commits.size());
    }

    @Test
    public void testFetchPullsSuccess() {
        wireMockExtension.stubFor(get("/repos/octocat/Hello-World/pulls")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile("github/pulls.json")));

        Flux<GitHubPullResponse> response = gitHubClient.fetchPulls("octocat", "Hello-world");
        List<GitHubPullResponse> pulls = response.collectList().block();

        assertEquals(30, pulls.size());
    }

    @Test
    public void testFetchPullsEmpty() {
        wireMockExtension.stubFor(get("/repos/Z3N/async_rust/pulls")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("[]")));

        Flux<GitHubPullResponse> response = gitHubClient.fetchPulls("Z3N", "async_rust");
        List<GitHubPullResponse> pulls = response.collectList().block();

        assertTrue(pulls.isEmpty());
    }

    @Test
    public void testFetchIssuesSize() {
        wireMockExtension.stubFor(get("/repos/octocat/Hello-World/issues")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile("github/issues.json")));

        Flux<GitHubIssueResponse> response = gitHubClient.fetchIssues("octocat", "hello-world");
        List<GitHubIssueResponse> issues = response.collectList().block();

        assertEquals(30, issues.size());
    }
}
