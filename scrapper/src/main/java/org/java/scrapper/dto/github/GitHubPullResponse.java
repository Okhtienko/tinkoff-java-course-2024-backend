package org.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GitHubPullResponse {
    Long id;

    String url;

    Integer number;

    String state;

    String title;

    GitHubOwnerResponse user;

    String body;

    @JsonProperty("created_at")
    OffsetDateTime createdDate;

    @JsonProperty("updated_at")
    OffsetDateTime updatedDate;

    @JsonProperty("closed_at")
    OffsetDateTime closedDate;

    @JsonProperty("merged_at")
    OffsetDateTime mergedDate;
}
