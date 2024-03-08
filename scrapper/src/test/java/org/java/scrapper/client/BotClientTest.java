package org.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.java.scrapper.dto.LinkUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Arrays;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
public class BotClientTest {

    @Autowired
    private BotClient botClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("bot-base-url", wireMockExtension::baseUrl);
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
                .withStatus(200)
                .withBody(expectedResponse)));

        Mono<String> response = botClient.sendUpdate(request);
        assertEquals(expectedResponse, response.block());
    }

    @Test
    public void testSendUpdateError() throws JsonProcessingException {
        LinkUpdateRequest request = new LinkUpdateRequest()
            .setUrl("https://www.example.com/")
            .setDescription("This is an updated link description.");

        String expectedRequest = mapper.writeValueAsString(request);

        wireMockExtension.stubFor(post("/updates")
            .withRequestBody(equalToJson(expectedRequest))
            .willReturn(aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withStatus(500)
                .withBody("Internal Server Error")));

        Mono<String> response = botClient.sendUpdate(request);

        StepVerifier.create(response)
            .expectError(WebClientResponseException.InternalServerError.class)
            .verify();
    }
}
