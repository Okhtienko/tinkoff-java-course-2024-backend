package org.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GitHubIssueResponse {
    Long id;

    Integer number;

    String title;

    String body;

    String state;

    GitHubOwnerResponse user;

    GitHubOwnerResponse assignee;

    Integer comments;

    @JsonProperty("created_at")
    OffsetDateTime createdDate;

    @JsonProperty("updated_at")
    OffsetDateTime updatedDate;

    @JsonProperty("closed_at")
    String closedDate;
}
