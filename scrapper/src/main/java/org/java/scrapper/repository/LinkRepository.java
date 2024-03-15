package org.java.scrapper.repository;

import java.net.URISyntaxException;
import java.util.List;
import org.java.scrapper.dto.LinkResponse;

public interface LinkRepository {
    void register(Long id);

    void delete(Long id);

    List<String> gets(Long id);

    LinkResponse save(Long id, String url) throws URISyntaxException;

    void remove(Long id, String url) throws URISyntaxException;
}
