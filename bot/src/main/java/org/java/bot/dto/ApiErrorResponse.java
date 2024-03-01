package org.java.bot.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class ApiErrorResponse {
    private String description;
    private Integer code;
    private String name;
    private String message;
    private List<StackTraceElement> stacktrace;
}
