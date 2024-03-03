package org.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class GitHubPullResponse {
    private Long id;

    private String url;

    private Integer number;

    private String state;

    private String title;

    private GitHubOwnerResponse user;

    private String body;

    @JsonProperty("created_at")
    private OffsetDateTime createdDate;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedDate;

    @JsonProperty("closed_at")
    private OffsetDateTime closedDate;

    @JsonProperty("merged_at")
    private OffsetDateTime mergedDate;
}
