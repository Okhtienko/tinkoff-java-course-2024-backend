package org.java.scrapper.dto.github;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class GitHubCommitResponse {
    private String sha;
    private String message;
    private String url;
    private GitHubOwnerResponse author;
}
