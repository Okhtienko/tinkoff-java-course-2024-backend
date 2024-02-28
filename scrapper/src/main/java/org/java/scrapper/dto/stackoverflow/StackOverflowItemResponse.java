package org.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class StackOverflowItemResponse {
    @JsonProperty("question_id")
    Long id;

    String title;

    StackOverflowOwnerResponse owner;

    @JsonProperty("last_activity_date")
    OffsetDateTime lastActivityDate;

    @JsonProperty("creation_date")
    OffsetDateTime creationDate;

    @JsonProperty("last_edit_date")
    OffsetDateTime lastEditDate;
}
