package org.java.bot.client;

import org.java.bot.dto.LinkRequest;
import org.java.bot.dto.LinkResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ScrapperClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8080/api/v1/scrapper";

    private final WebClient webClient;

    public ScrapperClient(@Value("${service.scrapper-base-url}") String scrapperBaseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(scrapperBaseUrl == null || scrapperBaseUrl.isEmpty() ? DEFAULT_BASE_URL : scrapperBaseUrl)
            .build();
    }

    public Mono<String> register(Long id) {
        return webClient.post()
            .uri("/{id}", id)
            .retrieve()
            .bodyToMono(String.class);
    }

    public Mono<String> delete(Long id) {
        return webClient.delete()
            .uri("/{id}", id)
            .retrieve()
            .bodyToMono(String.class);
    }

    public Flux<LinkResponse> gets(Long id) {
        return webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .retrieve()
            .bodyToFlux(LinkResponse.class);
    }

    public Mono<LinkResponse> save(Long id, String url) {
        LinkRequest request = new LinkRequest().setUrl(url.toString());
        return webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<LinkResponse> remove(Long id, String url) {
        LinkRequest request = new LinkRequest().setUrl(url.toString());
        return webClient.delete()
            .uri(uriBuilder -> uriBuilder.path("/links").queryParam("url", request.getUrl()).build())
            .header("Tg-Chat-Id", String.valueOf(id))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }
}
