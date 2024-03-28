package org.java.scrapper.model;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Link {
    private Long id;
    private String url;
    private String createdBy;
    private OffsetDateTime lastCheck;
    private OffsetDateTime createdAt;
}
