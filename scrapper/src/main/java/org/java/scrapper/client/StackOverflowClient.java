package org.java.scrapper.client;

import lombok.extern.slf4j.Slf4j;
import org.java.scrapper.dto.stackoverflow.StackOverflowAnswerResponse;
import org.java.scrapper.dto.stackoverflow.StackOverflowQuestionResponse;
import org.java.scrapper.repository.StackOverflowRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class StackOverflowClient implements StackOverflowRepository {
    private final WebClient stackOverflowClient;

    public StackOverflowClient(@Qualifier("stackOverflowWebClient") WebClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public Mono<StackOverflowQuestionResponse> fetchQuestion(Long id) {
        log.info("Fetching question with ID: {}", id);
        return stackOverflowClient.get()
            .uri("/questions/{id}?order=desc&sort=activity&site=stackoverflow", id)
            .retrieve()
            .bodyToMono(StackOverflowQuestionResponse.class);
    }

    @Override
    public Flux<StackOverflowAnswerResponse> fetchAnswers(Long id) {
        log.info("Fetching answers for question with ID: {}", id);
        return stackOverflowClient.get()
            .uri("/questions/{id}/answers?order=desc&sort=activity&site=stackoverflow", id)
            .retrieve()
            .bodyToFlux(StackOverflowAnswerResponse.class);
    }
}
