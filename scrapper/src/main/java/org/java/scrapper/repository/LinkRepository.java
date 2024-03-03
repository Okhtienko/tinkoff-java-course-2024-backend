package org.java.scrapper.repository;

import java.net.URISyntaxException;
import java.util.List;
import org.java.scrapper.dto.LinkResponse;

public interface LinkRepository {
    void register(Long id);

    void delete(Long id);

    List<LinkResponse> gets(Long id);

    LinkResponse save(Long id, String url) throws URISyntaxException;

    LinkResponse remove(Long id, String url) throws URISyntaxException;
}
