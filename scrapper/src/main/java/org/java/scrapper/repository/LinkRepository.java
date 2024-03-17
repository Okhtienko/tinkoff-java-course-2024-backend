package org.java.scrapper.repository;

import java.net.URISyntaxException;
import java.util.List;
import org.java.scrapper.dto.link.LinkRequest;
import org.java.scrapper.dto.link.LinkResponse;

public interface LinkRepository {
    LinkResponse save(LinkRequest request, Long chatId) throws URISyntaxException;

    LinkResponse get(LinkRequest request, Long chatId);

    void remove(String url, Long chatId);

    List<LinkResponse> gets(Long chatId);
}
