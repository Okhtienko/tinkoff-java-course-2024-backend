package org.java.scrapper.dto.chat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Accessors(chain = true)
@Getter
@Setter
public class ChatRequest {

    @NotNull(message = "ChatId may not be null")
    private Long chatId;

    @NotBlank(message = "Name may not be blank")
    private String name;

    @NotBlank(message = "CreatedBy may not be blank")
    private String createdBy;
}
