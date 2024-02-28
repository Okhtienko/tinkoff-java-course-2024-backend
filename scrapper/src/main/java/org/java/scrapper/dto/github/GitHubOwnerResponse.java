package org.java.scrapper.dto.github;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GitHubOwnerResponse {
    Long id;
    String login;
}
