package org.java.scrapper.domain;

import java.time.OffsetDateTime;
import java.util.List;
import org.java.scrapper.dto.link.LinkRequest;
import org.java.scrapper.dto.link.LinkResponse;

public interface LinkManagementService {
    LinkResponse get(LinkRequest request, Long chatId);

    LinkResponse save(LinkRequest request, Long chatId);

    LinkResponse remove(LinkRequest request, Long chatId);

    List<LinkResponse> gets(Long chatId);

    List<LinkResponse> getsByLastCheck(Long delay);

    List<Long> getsChatByLastCheck(Long delay, LinkResponse link);

    void updateLastCheck(LinkResponse response, OffsetDateTime date);
}
