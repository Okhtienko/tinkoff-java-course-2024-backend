package org.java.scrapper.client;

import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.datamanager.StackOverflowDataManager;
import org.java.scrapper.dto.stackoverflow.StackOverflowAnswerResponse;
import org.java.scrapper.dto.stackoverflow.StackOverflowQuestionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class StackOverflowClient implements StackOverflowDataManager {
    private static final String DEFAULT_STACKOVERFLOW_URL = "https://api.stackexchange.com/2.3";
    private final WebClient webClient;

    public StackOverflowClient(@Value("$client.stackoverflow-base-url") String stackOverflowBaseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(stackOverflowBaseUrl == null || stackOverflowBaseUrl.isEmpty()
                ? DEFAULT_STACKOVERFLOW_URL
                : stackOverflowBaseUrl)
            .build();
    }

    @Override
    public Mono<StackOverflowQuestionResponse> fetchQuestion(Long id) {
        log.info("Fetching question with ID: {}", id);
        return webClient.get()
            .uri("/questions/{id}?order=desc&sort=activity&site=stackoverflow", id)
            .retrieve()
            .bodyToMono(StackOverflowQuestionResponse.class);
    }

    @Override
    public Flux<StackOverflowAnswerResponse> fetchAnswers(Long id) {
        log.info("Fetching answers for question with ID: {}", id);
        return webClient.get()
            .uri("/questions/{id}/answers?order=desc&sort=activity&site=stackoverflow", id)
            .retrieve()
            .bodyToFlux(StackOverflowAnswerResponse.class);
    }
}
