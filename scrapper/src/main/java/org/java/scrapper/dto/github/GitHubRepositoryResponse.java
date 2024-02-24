package org.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GitHubRepositoryResponse {
    Long id;

    String name;

    String description;

    @JsonProperty("created_at")
    OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    OffsetDateTime updatedAt;
}
