package org.java.bot.client;

import org.java.bot.dto.LinkRequest;
import org.java.bot.dto.LinkResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ScrapperClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8080/api/v1/scrapper";
    private static final String URL_LINKS = "/links";
    private static final String TG_HEADER = "Tg-Chat-Id";
    private static final String URL_ID = "/{id}";

    private final WebClient webClient;

    public ScrapperClient(@Value("${service.scrapper-base-url}") String scrapperBaseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(scrapperBaseUrl == null || scrapperBaseUrl.isEmpty() ? DEFAULT_BASE_URL : scrapperBaseUrl)
            .build();
    }

    public Mono<String> register(Long id) {
        return webClient.post()
            .uri(URL_ID, id)
            .retrieve()
            .bodyToMono(String.class);
    }

    public Mono<String> delete(Long id) {
        return webClient.delete()
            .uri(URL_ID, id)
            .retrieve()
            .bodyToMono(String.class);
    }

    public Flux<LinkResponse> gets(Long id) {
        return webClient.get()
            .uri(URL_LINKS)
            .header(TG_HEADER, String.valueOf(id))
            .retrieve()
            .bodyToFlux(LinkResponse.class);
    }

    public Mono<LinkResponse> save(Long id, LinkRequest request) {
        return webClient.post()
            .uri(URL_LINKS)
            .header(TG_HEADER, String.valueOf(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<Void> remove(Long id,  LinkRequest request) {
        return webClient.delete()
            .uri(URL_LINKS + "/{url}", request.getUrl())
            .header(TG_HEADER, String.valueOf(id))
            .retrieve()
            .bodyToMono(Void.class);
    }
}
