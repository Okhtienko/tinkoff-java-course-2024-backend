package org.java.scrapper.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.java.scrapper.dto.github.GitHubCommitResponse;
import org.java.scrapper.dto.github.GitHubIssueResponse;
import org.java.scrapper.dto.github.GitHubPullResponse;
import org.java.scrapper.dto.github.GitHubRepositoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class GitHubControllerTest {

    @Autowired
    private GitHubController gitHubController;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("github-base-url", wireMockExtension::baseUrl);
    }

    @Test
    public void testFetchRepository() {
        stubGetResponse("/api/v1/scrapper/github/repository/octocat/Hello-World", "github/repository.json");
        Mono<GitHubRepositoryResponse> response = gitHubController.fetchRepository(
                "octocat",
                "hello-world"
        );

        StepVerifier.create(response)
                .expectNextMatches(repository -> repository.getName().equals("Hello-World"))
                .verifyComplete();
    }

    @Test
    public void testFetchPullsSize() {
        stubGetResponse("/api/v1/scrapper/github/pulls/octocat/Hello-World", "github/pulls.json");

        Flux<GitHubPullResponse> response = gitHubController.fetchPulls("octocat", "hello-world");
        List<GitHubPullResponse> pulls = response.collectList().block();
        assertNotNull(pulls);
        assertEquals(30, pulls.size());
    }

    @Test
    public void testFetchCommitsSize() {
        stubGetResponse("/api/v1/scrapper/github/commits/octocat/Hello-World", "github/commits.json");

        Flux<GitHubCommitResponse> response = gitHubController.fetchCommits("octocat", "hello-world");
        List<GitHubCommitResponse> commits = response.collectList().block();
        assertNotNull(commits);
        assertEquals(3, commits.size());
    }

    @Test
    public void testFetchIssuesSize() {
        stubGetResponse("/api/v1/scrapper/github/issues/octocat/Hello-World", "github/issues.json");

        Flux<GitHubIssueResponse> response = gitHubController.fetchIssues("octocat", "hello-world");
        List<GitHubIssueResponse> issues = response.collectList().block();
        assertNotNull(issues);
        assertEquals(30, issues.size());
    }

    private void stubGetResponse(String url, String body) {
        wireMockExtension.stubFor(get(url)
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile(body)));
    }
}
