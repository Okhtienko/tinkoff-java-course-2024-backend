package org.java.scrapper.configuration;

import lombok.Data;
import org.java.scrapper.client.GitHubClient;
import org.java.scrapper.client.StackOverflowClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "client")
@Data
public class ClientConfiguration {
    private String githubBaseUrl;
    private String stackOverflowBaseUrl;

    @Bean
    public GitHubClient gitHubWebClient() {
        return new GitHubClient(githubBaseUrl);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(stackOverflowBaseUrl);
    }
}
