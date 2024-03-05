package org.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.java.scrapper.dto.stackoverflow.StackOverflowAnswerResponse;
import org.java.scrapper.dto.stackoverflow.StackOverflowItemResponse;
import org.java.scrapper.dto.stackoverflow.StackOverflowQuestionResponse;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
public class StackOverflowClientTest {

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("stackoverflow-base-url", wireMockExtension::baseUrl);
    }

    @Test
    public void testFetchQuestionSuccess() {
        wireMockExtension.stubFor(get("/questions/123?order=desc&sort=activity&site=stackoverflow")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("stacoverflow/question.json")));

        Mono<StackOverflowQuestionResponse> response = stackOverflowClient.fetchQuestion(123L);

        StepVerifier.create(response)
            .expectNextMatches(question ->
                question.getItems()
                    .get(0)
                    .getTitle()
                    .equals("Java lib or app to convert CSV to XML file?")
                    && question.getItems()
                    .get(0)
                    .getOwner()
                    .getUserId() == 78
            )
            .verifyComplete();
    }

    @Test
    public void testFetchQuestionEmpty() {
        wireMockExtension.stubFor(get("/questions/54321?order=desc&sort=activity&site=stackoverflow")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{}")));

        Mono<StackOverflowQuestionResponse> response = stackOverflowClient.fetchQuestion(54321L);

        StepVerifier.create(response)
            .expectNextMatches(question -> question.getItems().isEmpty())
            .expectComplete()
            .verify();
    }

    @Test
    public void testFetchAnswersSuccess() {
        wireMockExtension.stubFor(get("/questions/78031160/answers?order=desc&sort=activity&site=stackoverflow")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("stackoverflow/answers.json")));

        Flux<StackOverflowAnswerResponse> response = stackOverflowClient.fetchAnswers(78031160L);
        List<StackOverflowItemResponse> answers = response.collectList().block().get(0).getItems();

        assertEquals(2, answers.size());
    }

    @Test
    public void testFetchAnswersEmpty() {
        wireMockExtension.stubFor(get("/questions/54321/answers?order=desc&sort=activity&site=stackoverflow")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{}")));

        Flux<StackOverflowAnswerResponse> response = stackOverflowClient.fetchAnswers(54321L);

        StepVerifier.create(response)
            .expectNextMatches(answer -> answer.getItems().isEmpty())
            .expectComplete()
            .verify();
    }
}
