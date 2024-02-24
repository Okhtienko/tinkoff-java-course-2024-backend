package org.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class StackOverflowOwnerResponse {
    @JsonProperty("account_id")
    Long accountId;

    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("user_type")
    String type;

    @JsonProperty("display_name")
    String name;
}
