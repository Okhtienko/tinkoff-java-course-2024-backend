package org.java.bot.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Accessors(chain = true)
@Getter
@Setter
public class LinkUpdateRequest {

    @NotNull(message = "ID may not be null")
    private Long id;

    @NotBlank(message = "URL may not be blank")
    private String url;

    @Size(max = 255, message = "Description may not exceed 255 characters")
    @NotBlank(message = "URL may not be blank")
    private String description;

    @NotEmpty(message = "Chats may not be empty")
    @NotNull(message = "Chats may not be null")
    private List<Long> chats;

}
