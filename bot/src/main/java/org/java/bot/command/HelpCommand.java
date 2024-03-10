package org.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.aspect.BotCommand;
import org.java.bot.bot.Command;
import org.java.bot.component.CommandHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HelpCommand implements Command {
    private static final String COMMAND = "/help";
    private static final String HELP_DESCRIPTION = "display help menu";

    private CommandHolder commands;

    @Autowired
    public HelpCommand(@Lazy CommandHolder commands) {
        this.commands = commands;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return HELP_DESCRIPTION;
    }

    @BotCommand
    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        log.info("Processing help command for chat ID: {}", chatId);

        String message = toStringCommands(commands.gets());

        log.info("Sending help message to chat ID: {}", chatId);
        return new SendMessage(chatId, message);
    }

    private String toStringCommands(Map<String, Command> commands) {
        return commands.entrySet().stream()
            .map(entry -> entry.getKey() + " - " + entry.getValue().description())
            .collect(Collectors.joining("\n", "Available commands:\n\n", ""));
    }
}
