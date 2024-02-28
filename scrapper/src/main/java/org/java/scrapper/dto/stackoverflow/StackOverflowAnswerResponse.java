package org.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class StackOverflowAnswerResponse {
    List<StackOverflowItemResponse> items;

    @JsonProperty("quota_max")
    Integer quota;
}
