package org.java.scrapper.repository;

import java.util.List;
import org.java.scrapper.model.Chat;

public interface ChatRepository {
    void save(Long id, String name, String createdBy);

    void delete(Long id);

    Chat get(Long id);

    List<Chat> gets();

    Boolean exists(Long id);
}
