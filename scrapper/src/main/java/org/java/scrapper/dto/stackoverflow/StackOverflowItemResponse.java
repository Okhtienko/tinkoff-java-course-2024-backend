package org.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class StackOverflowItemResponse {
    @JsonProperty("question_id")
    private Long id;

    private String title;

    private StackOverflowOwnerResponse owner;

    @JsonProperty("last_activity_date")
    private OffsetDateTime lastActivityDate;

    @JsonProperty("creation_date")
    private OffsetDateTime creationDate;

    @JsonProperty("last_edit_date")
    private OffsetDateTime lastEditDate;
}
