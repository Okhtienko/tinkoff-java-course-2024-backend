package org.java.scrapper.configuration;

import org.java.scrapper.client.BotClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {
    @Value("${service.bot-base-url}")
    private String scrapperBaseUrl;

    @Bean
    public BotClient scrapperClient() {
        return new BotClient(scrapperBaseUrl);
    }
}
