package org.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import org.java.scrapper.client.StackOverflowClient;
import org.java.scrapper.dto.stackoverflow.StackOverflowAnswerResponse;
import org.java.scrapper.dto.stackoverflow.StackOverflowQuestionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/scrapper/stackoverflow")
@RequiredArgsConstructor
public class StackOverflowController {
    private final StackOverflowClient stackOverflowClient;

    @GetMapping("/question/{id}")
    public Mono<StackOverflowQuestionResponse> fetchQuestion(@PathVariable Long id) {
        return stackOverflowClient.fetchQuestion(id);
    }

    @GetMapping("/answers/{id}")
    public Flux<StackOverflowAnswerResponse> fetchAnswers(@PathVariable Long id) {
        return stackOverflowClient.fetchAnswers(id);
    }
}
