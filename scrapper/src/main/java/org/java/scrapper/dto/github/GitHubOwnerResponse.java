package org.java.scrapper.dto.github;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class GitHubOwnerResponse {
    private Long id;
    private String login;
}
