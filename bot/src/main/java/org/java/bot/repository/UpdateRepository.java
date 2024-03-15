package org.java.bot.repository;

import org.java.bot.dto.LinkUpdateRequest;

public interface UpdateRepository {
    void sendUpdate(LinkUpdateRequest request);
}
