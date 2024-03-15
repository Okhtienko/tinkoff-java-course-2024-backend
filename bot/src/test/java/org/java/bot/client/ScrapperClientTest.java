package org.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.java.bot.dto.ApiErrorResponse;
import org.java.bot.dto.LinkRequest;
import org.java.bot.dto.LinkResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
@Disabled
public class ScrapperClientTest {
    @Autowired
    private ScrapperClient scrapperClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("scrapper-base-url", wireMockExtension::baseUrl);
    }

    @Test
    public void testRegisterSuccess() {
        Long id = 123L;
        String expectedResponse = "Registration successful";

        wireMockExtension.stubFor(post(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedResponse)));

        Mono<String> response = scrapperClient.register(id);

        StepVerifier.create(response)
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    public void testRegisterConflict() throws JsonProcessingException {
        Long id = 123L;

        ApiErrorResponse error = buildErrorResponse(
            "Chat already registered",
            409,
            "ConflictException",
            "Chat already registered"
        );

        String expectedError = mapper.writeValueAsString(error);

        wireMockExtension.stubFor(post(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(409)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedError)));

        Mono<String> response = scrapperClient.register(id);

        StepVerifier.create(response)
            .expectErrorMessage("Chat already registered")
            .verify();
    }

    @Test
    public void testRegisterError() throws JsonProcessingException {
        Long id = null;

        ApiErrorResponse error = buildErrorResponse(
            "Internal Server Error",
            500,
            "InternalServerError",
            "Internal Server Error"
        );

        String expectedError = mapper.writeValueAsString(error);

        wireMockExtension.stubFor(post(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedError)));

        Mono<String> response = scrapperClient.register(id);

        StepVerifier.create(response)
            .expectErrorMessage("Internal Server Error")
            .verify();
    }

    @Test
    public void testSaveSuccess() throws URISyntaxException, JsonProcessingException {
        Long id = 123L;
        LinkRequest request = new LinkRequest().setUrl("https://example.com/new-link");
        LinkResponse link = new LinkResponse().setId(1L).setUrl(new URI("https://example.com/new-link"));
        String expectedResponse = mapper.writeValueAsString(link);

        wireMockExtension.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(mapper.writeValueAsString(expectedResponse))));

        Mono<LinkResponse> response = scrapperClient.save(id, request);
        assertEquals(response.block().getUrl(), link.getUrl());
    }

    @Test
    public void testSaveError() throws JsonProcessingException {
        Long id = 123L;
        LinkRequest request = new LinkRequest().setUrl("link");

        ApiErrorResponse error = buildErrorResponse(
            "Invalid request parameters",
            400,
            "BadRequestException",
            "Invalid request parameters"
        );

        String expectedError = mapper.writeValueAsString(error);

        wireMockExtension.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(400)
                .withBody(expectedError)));

        Mono<LinkResponse> response = scrapperClient.save(id, request);

        StepVerifier.create(response)
            .expectErrorMessage("Invalid request parameters")
            .verify();
    }

    @Test
    public void testGetsSuccess() throws URISyntaxException, JsonProcessingException {
        Long id = 123L;
        LinkResponse link = new LinkResponse().setId(1L).setUrl(new URI("https://example.com/new-link"));
        List<LinkResponse> links = List.of(link);
        String expectedResponse = mapper.writeValueAsString(links);

        wireMockExtension.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(expectedResponse)));

        Flux<String> response = scrapperClient.gets(id);

        StepVerifier.create(response)
            .expectNextCount(links.size())
            .verifyComplete();
    }

    @Test
    public void testGetsError() throws JsonProcessingException {
        Long id = 456L;

        ApiErrorResponse error = buildErrorResponse(
            "Invalid Chat ID",
            400,
            "BadRequestException",
            "Invalid Chat ID"
        );

        String expectedError = mapper.writeValueAsString(error);

        wireMockExtension.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedError)));

        Flux<String> response = scrapperClient.gets(id);

        StepVerifier.create(response)
            .expectErrorMessage("Invalid Chat ID")
            .verify();
    }

    @Test
    public void testDeleteNotFound() throws JsonProcessingException {
        Long id = 456L;

        ApiErrorResponse error = buildErrorResponse(
            "Chat not found",
            404,
            "NotFoundException",
            "Chat not found"
        );

        String expectedError = mapper.writeValueAsString(error);

        wireMockExtension.stubFor(delete(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedError)));

        Mono<Void> response = scrapperClient.delete(id);

        StepVerifier.create(response)
            .expectErrorMessage("Chat not found")
            .verify();
    }

    @Test
    public void testDeleteError() throws JsonProcessingException {
        Long id = null;

        ApiErrorResponse error = buildErrorResponse(
            "Internal Server Error",
            500,
            "InternalServerError",
            "Internal Server Error"
        );

        String expectedError = mapper.writeValueAsString(error);

        wireMockExtension.stubFor(delete(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedError)));

        Mono<Void> response = scrapperClient.delete(id);

        StepVerifier.create(response)
            .expectErrorMessage("Internal Server Error")
            .verify();
    }

    @Test
    public void testDeleteSuccess() {
        Long id = 123L;

        wireMockExtension.stubFor(delete(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(204)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        Mono<Void> response = scrapperClient.delete(id);

        StepVerifier.create(response)
            .verifyComplete();
    }

    private ApiErrorResponse buildErrorResponse(String description, Integer code, String name, String message) {
        return new ApiErrorResponse()
            .setDescription(description)
            .setCode(code)
            .setName(name)
            .setMessage(message)
            .setStackTrace(Collections.emptyList());
    }
}
