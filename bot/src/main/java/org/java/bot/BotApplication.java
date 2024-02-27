package org.java.bot;

import lombok.RequiredArgsConstructor;
import org.java.bot.configuration.ApplicationConfig;
import org.java.bot.handler.LinkTrackerBot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@RequiredArgsConstructor
@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication implements CommandLineRunner {
    private final LinkTrackerBot linkTrackerBot;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    @Override
    public void run(String... args) {
        linkTrackerBot.start();
    }
}
