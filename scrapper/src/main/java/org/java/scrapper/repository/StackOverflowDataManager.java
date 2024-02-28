package org.java.scrapper.repository;

import org.java.scrapper.dto.stackoverflow.StackOverflowAnswerResponse;
import org.java.scrapper.dto.stackoverflow.StackOverflowQuestionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StackOverflowDataManager {
    Mono<StackOverflowQuestionResponse> fetchQuestion(Long id);

    Flux<StackOverflowAnswerResponse> fetchAnswers(Long id);
}
