package org.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class StackOverflowQuestionResponse {
    private List<StackOverflowItemResponse> items;

    @JsonProperty("quota_max")
    private Integer quota;
}
