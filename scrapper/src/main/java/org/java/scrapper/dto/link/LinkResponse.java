package org.java.scrapper.dto.link;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class LinkResponse {
    private Long id;
    private URI url;
    private String createdBy;
    private OffsetDateTime lastCheck;
    private OffsetDateTime createdAt;
}
