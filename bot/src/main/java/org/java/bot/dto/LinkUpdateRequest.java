package org.java.bot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
public class LinkUpdateRequest {
    private Long id;
    private String url;
    private String description;
    private List<Long> chats;
}
