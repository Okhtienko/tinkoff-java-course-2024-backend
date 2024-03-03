package org.java.scrapper.client;

import org.java.scrapper.dto.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
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
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(String.class);
    }
}
