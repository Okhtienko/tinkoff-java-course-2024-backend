package org.java.scrapper.dto.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class ChatResponse {
    private Long id;
    private Long chatId;
    private String name;
    private String createdBy;
}
