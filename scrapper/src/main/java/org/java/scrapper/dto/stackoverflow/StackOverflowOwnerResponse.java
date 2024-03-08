package org.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class StackOverflowOwnerResponse {
    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_type")
    private String type;

    @JsonProperty("display_name")
    private String name;
}
