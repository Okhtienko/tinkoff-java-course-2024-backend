package org.java.bot.component;

import com.pengrad.telegrambot.model.BotCommand;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.bot.Command;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Menu {
    private BotCommand[] commands;

    public Menu(CommandHolder commandHolder) {
        log.info("Initializing Menu.");
        this.commands = convertToBotCommands(commandHolder);
        log.info("Menu commands initialized successfully.");
    }

    public BotCommand[] get() {
        log.info("Retrieving menu commands.");
        return commands;
    }

    private BotCommand[] convertToBotCommands(CommandHolder commands) {
        log.info("Converting commands to BotCommand objects.");
        return commands.gets().values().stream()
            .map(this::toBotCommand)
            .toArray(BotCommand[]::new);
    }

    private BotCommand toBotCommand(Command command) {
        return new BotCommand(command.command(), command.description());
    }
}
