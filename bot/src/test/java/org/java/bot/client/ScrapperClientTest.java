package org.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.java.bot.dto.LinkRequest;
import org.java.bot.dto.LinkResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.net.URI;
import java.net.URISyntaxException;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    public void testRegisterSuccess() {
        Long id = 1L;
        String expectedResponse = "Registration successful";

        wireMockExtension.stubFor(post(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedResponse)));

        Mono<String> response = scrapperClient.register(id);
        assertEquals(response.block(), expectedResponse);
    }

    @Test
    @Order(2)
    public void testRegisterConflict() {
        Long id = 1L;

        wireMockExtension.stubFor(post(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(409)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("Conflict")));
        Mono<String> response = scrapperClient.register(id);

        StepVerifier.create(response)
            .expectError(WebClientResponseException.Conflict.class)
            .verify();
    }

    @Test
    @Order(3)
    public void testRegisterError() {
        Long id = null;

        wireMockExtension.stubFor(post(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("Internal Server Error")));

        Mono<String> response = scrapperClient.register(id);

        StepVerifier.create(response)
            .expectError(WebClientResponseException.InternalServerError.class)
            .verify();
    }

    @Test
    @Order(4)
    public void testSaveLinkSuccess() throws URISyntaxException, JsonProcessingException {
        Long id = 1L;
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
    @Order(5)
    public void testSaveLinkError() {
        Long id = 1L;
        LinkRequest request = new LinkRequest().setUrl(null);

        wireMockExtension.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(500)
                .withBody("Internal Server Error")));

        Mono<LinkResponse> response = scrapperClient.save(id, request);

        StepVerifier.create(response)
            .expectError(WebClientResponseException.InternalServerError.class)
            .verify();
    }

    @Test
    @Order(6)
    public void testGetLinksSuccess() throws URISyntaxException, JsonProcessingException {
        Long id = 1L;
        LinkResponse link = new LinkResponse().setId(1L).setUrl(new URI("https://example.com/new-link"));
        List<LinkResponse> links = List.of(link);
        String expectedResponse = mapper.writeValueAsString(links);

        wireMockExtension.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(expectedResponse)));

        Flux<LinkResponse> response = scrapperClient.gets(id);
        assertEquals(response.collectList().block().get(0).getUrl(), links.get(0).getUrl());
    }

    @Test
    @Order(7)
    public void testGetLinksError() {
        Long id = 2L;

        wireMockExtension.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("Bad Request")));

        Flux<LinkResponse> response = scrapperClient.gets(id);

        StepVerifier.create(response)
            .expectError(WebClientResponseException.BadRequest.class)
            .verify();
    }

    @Test
    @Order(8)
    public void testRemoveLinkError() {
        Long id = 1L;
        LinkRequest request = new LinkRequest().setUrl("");

        wireMockExtension.stubFor(post(urlEqualTo("/links/"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(500)
                .withBody("Internal Server Error")));

        Mono<Void> response = scrapperClient.remove(id, request);

        StepVerifier.create(response)
            .expectError(WebClientResponseException.InternalServerError.class)
            .verify();
    }

    @Test
    @Order(9)
    public void testDeleteSuccess() {
        Long id = 1L;
        String expectedResponse = "Deletion successful";

        wireMockExtension.stubFor(delete(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedResponse)));

        Mono<String> response = scrapperClient.delete(id);
        assertEquals(response.block(), expectedResponse);
    }

    @Test
    @Order(10)
    public void testDeleteNotFound() {
        Long id = 2L;

        wireMockExtension.stubFor(delete(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("Not Found")));

        Mono<String> response = scrapperClient.delete(id);

        StepVerifier.create(response)
            .expectError(WebClientResponseException.NotFound.class)
            .verify();
    }

    @Test
    @Order(11)
    public void testDeleteError() {
        Long id = null;

        wireMockExtension.stubFor(delete(urlEqualTo("/" + id))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("Internal Server Error")));

        Mono<String> response = scrapperClient.delete(id);

        StepVerifier.create(response)
            .expectError(WebClientResponseException.InternalServerError.class)
            .verify();
    }
}
