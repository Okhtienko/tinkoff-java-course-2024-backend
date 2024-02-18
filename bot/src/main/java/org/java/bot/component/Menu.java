package org.java.bot.component;

import com.pengrad.telegrambot.model.BotCommand;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.bot.Command;
import org.java.bot.handler.MessageProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Menu {
    private BotCommand[] commands;

    public Menu(MessageProcessor messageProcessor) {
        log.info("Initializing Menu.");
        this.commands = convertToBotCommands(messageProcessor.commands());
        log.info("Menu commands initialized successfully.");
    }

    public BotCommand[] get() {
        log.info("Retrieving menu commands.");
        return commands;
    }

    private BotCommand[] convertToBotCommands(List<Command> commandList) {
        log.info("Converting commands to BotCommand objects.");
        return commandList.stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new);
    }
}
