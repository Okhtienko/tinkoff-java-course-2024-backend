package org.java.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.dto.LinkUpdateRequest;
import org.java.bot.exception.BadRequestException;
import org.java.bot.repository.UpdateRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateService implements UpdateRepository {

    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        log.info("Received update request: {}", request);
        validateRequest(request);
    }

    private void validateRequest(LinkUpdateRequest request) {
        if (request.getId() == null || request.getUrl() == null) {
            throw new BadRequestException("Invalid request parameters");
        }
    }
}
