package org.java.scrapper.dto.link;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Accessors(chain = true)
@Getter
@Setter
public class LinkRequest {

    @NotBlank(message = "URL may not be blank")
    private String url;

    @NotBlank(message = "CreatedBy may not be blank")
    private String createdBy;

}
