package org.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class GitHubIssueResponse {
    private Long id;

    private Integer number;

    private String title;

    private String body;

    private String state;

    private GitHubOwnerResponse user;

    private GitHubOwnerResponse assignee;

    private Integer comments;

    @JsonProperty("created_at")
    private OffsetDateTime createdDate;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedDate;

    @JsonProperty("closed_at")
    private String closedDate;
}
