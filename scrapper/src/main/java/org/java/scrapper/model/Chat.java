package org.java.scrapper.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Chat {
    private Long id;
    private Long chatId;
    private String name;
    private String createdBy;
}
