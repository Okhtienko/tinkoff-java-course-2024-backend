package org.java.scrapper.controller;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class StackOverflowControllerTest {

    @Autowired
    private StackOverflowController stackOverflowController;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("stackoverflow-base-url", wireMockExtension::baseUrl);
    }

    @Test
    public void testFetchQuestionInvalidQuestionId() {
        Mono<StackOverflowQuestionResponse> response = stackOverflowController.fetchQuestion(-1L);

        StepVerifier.create(response)
            .expectNextMatches(question -> question.getItems().isEmpty())
            .verifyComplete();
    }

    @Test
    public void testFetchQuestion() {
        stubGetResponse("/api/v1/scrapper/stackoverflow/question/123", "stacoverflow/question.json");
        Mono<StackOverflowQuestionResponse> response = stackOverflowController.fetchQuestion(123L);

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
    public void testFetchAnswersInvalidQuestionId() {
        Flux<StackOverflowAnswerResponse> response = stackOverflowController.fetchAnswers(-1L);
        StepVerifier.create(response)
            .expectNextMatches(answers -> answers.getItems().isEmpty())
            .verifyComplete();
    }

    @Test
    public void testFetchAnswers() {
        stubGetResponse("/api/v1/scrapper/stackoverflow/answers/78031160", "stacoverflow/answers.json");

        Flux<StackOverflowAnswerResponse> response = stackOverflowController.fetchAnswers(78031160L);
        List<StackOverflowItemResponse> answers = response.collectList().block().get(0).getItems();
        assertNotNull(answers);
        assertEquals(2, answers.size());
    }

    private void stubGetResponse(String url, String body) {
        wireMockExtension.stubFor(get(url)
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile(body)));
    }
}
