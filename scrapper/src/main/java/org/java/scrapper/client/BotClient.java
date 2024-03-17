package org.java.scrapper.client;

import org.java.scrapper.dto.ApiErrorResponse;
import org.java.scrapper.dto.link.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BotClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8090/api/v1/bot";
    private final WebClient webClient;

    public BotClient(@Value("${service.bot-base-url}") String botBaseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(botBaseUrl == null || botBaseUrl.isEmpty() ? DEFAULT_BASE_URL : botBaseUrl)
            .build();
    }

    public Mono<String> sendUpdate(LinkUpdateRequest request) {
        return webClient.post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new RuntimeException(error.getDescription())))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new RuntimeException(error.getDescription())))
            )
            .bodyToMono(String.class);
    }
}
