package org.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.aspect.BotCommand;
import org.java.bot.bot.Command;
import org.java.bot.service.UserService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class
StartCommand implements Command {
    private static final String COMMAND = "/start";
    private static final String START_DESCRIPTION = "register a user";
    private static final String HELP_MESSAGE = "Type /help to view the list of commands.";

    private final UserService userService;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return START_DESCRIPTION;
    }

    @BotCommand
    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        Long userId = update.message().from().id();
        StringBuilder message = new StringBuilder();

        if (userService.isExists(userId)) {
            log.info("User {} is already registered.", userId);
            message.append("This user is already registered and has access to all bot features. " + HELP_MESSAGE);
        } else {
            userService.save(userId);
            log.info("User {} registered successfully.", userId);
            message.append("Welcome! You are now registered and have access to all bot features. " + HELP_MESSAGE);
        }

        return new SendMessage(chatId, message.toString());
    }
}
