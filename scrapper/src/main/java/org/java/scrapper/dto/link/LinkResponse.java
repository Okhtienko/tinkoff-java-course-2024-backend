package org.java.scrapper.dto.link;

import java.net.URI;
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
}
