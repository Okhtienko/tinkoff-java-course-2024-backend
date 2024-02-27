package org.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.aspect.BotCommand;
import org.java.bot.bot.Command;
import org.java.bot.handler.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelpCommand implements Command {
    private static final String COMMAND = "/help";
    private static final String HELP_DESCRIPTION = "display help menu";

    private CommandHandler handler;

    @Autowired
    public HelpCommand(@Lazy CommandHandler handler) {
        this.handler = handler;
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

        String message = handler.toStringCommands();

        log.info("Sending help message to chat ID: {}", chatId);
        return new SendMessage(chatId, message);
    }
}
