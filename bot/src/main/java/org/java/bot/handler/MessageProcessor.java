package org.java.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.bot.UserMessageProcessor;
import org.java.bot.service.BotStateService;
import org.java.bot.util.BotState;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProcessor implements UserMessageProcessor {
    private final CommandHandler commandHandler;
    private final BotStateService botStateService;

    @Override
    public SendMessage process(Update update) {
        log.info("Processing update: {}", update.message().text());
        BotState currentState = botStateService.get();

        return switch (currentState) {
            case WAITING_FOR_LINK_SAVE -> commandHandler.handleSaveLink(update);
            case WAITING_FOR_LINK_REMOVE -> commandHandler.handleRemoveLink(update);
            default -> commandHandler.handleCommandOrUnknown(update);
        };
    }
}
