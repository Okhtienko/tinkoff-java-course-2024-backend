package org.java.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.client.ScrapperClient;
import org.java.bot.dto.LinkUpdateRequest;
import org.java.bot.repository.UpdateRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateService implements UpdateRepository {
    private final ScrapperClient scrapperClient;

    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        validateRequest(request);
    }

    private void validateRequest(LinkUpdateRequest request) {
        if (request.getId() == null || request.getUrl() == null) {
            throw new IllegalArgumentException("Required fields (id, url) are missing");
        }
    }
}
