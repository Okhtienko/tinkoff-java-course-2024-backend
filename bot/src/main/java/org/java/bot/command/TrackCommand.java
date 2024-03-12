package org.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.aspect.BotCommand;
import org.java.bot.bot.Command;
import org.java.bot.service.BotStateService;
import org.java.bot.utils.BotState;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private static final String COMMAND = "/track";
    private static final String TRACK_DESCRIPTION = "start tracking a link";

    private final BotStateService botStateService;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return TRACK_DESCRIPTION;
    }

    @BotCommand
    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        log.info("Handling TrackCommand for chat ID: {}", chatId);

        botStateService.set(BotState.WAITING_FOR_LINK_SAVE);

        return new SendMessage(chatId, "Enter tracking link:");
    }
}
