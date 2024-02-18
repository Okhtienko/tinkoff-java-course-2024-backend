package org.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.aspect.BotCommand;
import org.java.bot.bot.Command;
import org.java.bot.handler.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelpCommand implements Command {
    private static final String COMMAND = "/help";
    private static final String HELP_DESCRIPTION = "display help menu";

    private MessageProcessor messageProcessor;

    @Autowired
    public HelpCommand(@Lazy MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
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

        List<Command> commands = messageProcessor.commands();
        String formattedCommands = formatCommands(commands);

        log.info("Sending help message to chat ID: {}", chatId);
        return new SendMessage(chatId, formattedCommands);
    }

    private String formatCommands(List<Command> commands) {
        return commands.stream()
            .map(command -> command.command() + " - " + command.description())
            .collect(Collectors.joining("\n", "Available commands:\n\n", ""));
    }
}
