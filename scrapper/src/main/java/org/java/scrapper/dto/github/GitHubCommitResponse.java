package org.java.scrapper.dto.github;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GitHubCommitResponse {
    String sha;
    String message;
    String url;
    GitHubOwnerResponse author;
}
