package org.java.scrapper.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConfigurationProperties(prefix = "client")
@Data
public class ClientConfiguration {
    private static final String DEFAULT_GITHUB_URL = "https://api.github.com";
    private static final String DEFAULT_STACKOVERFLOW_URL = "https://api.stackexchange.com/2.3";
    private String githubBaseUrl;
    private String stackOverflowBaseUrl;

    @Bean(name = "gitHubWebClient")
    public WebClient gitHubWebClient() {
        String url = githubBaseUrl != null || githubBaseUrl.isEmpty()
            ? githubBaseUrl
            : DEFAULT_GITHUB_URL;

        return WebClient.builder()
            .baseUrl(url)
            .build();
    }

   @Bean(name = "stackOverflowWebClient")
    public WebClient stackOverflowWebClient() {
       String url = githubBaseUrl != null || stackOverflowBaseUrl.isEmpty()
           ? stackOverflowBaseUrl
           : DEFAULT_STACKOVERFLOW_URL;

        return WebClient.builder()
            .baseUrl(url)
            .build();
    }
}
