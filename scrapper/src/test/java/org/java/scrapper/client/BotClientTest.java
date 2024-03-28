package org.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.java.scrapper.dto.ApiErrorResponse;
import org.java.scrapper.dto.link.LinkUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Arrays;
import java.util.Collections;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@WireMockTest
public class BotClientTest {

    @Autowired
    private BotClient botClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @BeforeEach
    void setUpBotClient() {
        String baseUrl = wireMockExtension.baseUrl();
        botClient = new BotClient(baseUrl);
    }

    @Test
    public void testSendUpdateSuccess() throws JsonProcessingException {
        LinkUpdateRequest request = new LinkUpdateRequest()
            .setId(1L)
            .setUrl("https://www.example.com/")
            .setDescription("This is an updated link description.")
            .setChats(Arrays.asList(1L, 2L));

        String expectedRequest = mapper.writeValueAsString(request);
        String expectedResponse = "Update processed successfully";

        wireMockExtension.stubFor(post("/updates")
            .withRequestBody(equalToJson(expectedRequest))
            .willReturn(aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withStatus(201)
                .withBody(expectedResponse)));

        Mono<String> response = botClient.sendUpdate(request);

        StepVerifier.create(response)
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    public void testSendUpdateClientError() throws JsonProcessingException {
        LinkUpdateRequest request = new LinkUpdateRequest();

        ApiErrorResponse exception = new ApiErrorResponse()
            .setDescription("Invalid request parameters")
            .setCode(400)
            .setName("BadRequestException")
            .setMessage("Invalid request parameters.")
            .setStackTrace(Collections.emptyList());

        String expectedRequest = mapper.writeValueAsString(request);
        String expectedException = mapper.writeValueAsString(exception);

        wireMockExtension.stubFor(post("/updates")
            .withRequestBody(equalToJson(expectedRequest))
            .willReturn(aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withStatus(400)
                .withBody(expectedException)));

        Mono<String> response = botClient.sendUpdate(request);

        StepVerifier.create(response)
            .expectErrorMessage("Invalid request parameters")
            .verify();
    }
}
