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
    private String githubBaseUrl;
    private String stackOverflowBaseUrl;

    @Bean(name = "gitHubWebClient")
    public WebClient gitHubWebClient() {
        return WebClient.builder()
            .baseUrl(githubBaseUrl)
            .build();
    }

   @Bean(name = "stackOverflowWebClient")
    public WebClient stackOverflowWebClient() {
        return WebClient.builder()
            .baseUrl(stackOverflowBaseUrl)
            .build();
    }
}
