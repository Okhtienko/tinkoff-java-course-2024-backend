package org.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.aspect.BotCommand;
import org.java.bot.bot.Command;
import org.java.bot.service.BotStateService;
import org.java.bot.util.BotState;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private static final String COMMAND = "/untrack";
    private static final String UNTRACK_DESCRIPTION = "stop tracking a link";

    private final BotStateService botStateService;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return UNTRACK_DESCRIPTION;
    }

    @BotCommand
    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        log.info("Handling UntrackCommand for chat ID: {}", chatId);
        botStateService.set(BotState.WAITING_FOR_LINK_REMOVE);
        return new SendMessage(chatId, "Enter untracking link:");
    }
}
