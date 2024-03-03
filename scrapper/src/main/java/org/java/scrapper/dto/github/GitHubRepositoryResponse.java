package org.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class GitHubRepositoryResponse {
    private Long id;

    private String name;

    private String description;

    @JsonProperty("created_at")
    private OffsetDateTime createdDate;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedDate;
}
