package org.java.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.bot.Command;
import org.java.bot.bot.UserMessageProcessor;
import org.java.bot.component.CommandHolder;
import org.java.bot.service.BotStateService;
import org.java.bot.service.LinkService;
import org.java.bot.util.BotState;
import org.java.bot.util.MessageUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProcessor implements UserMessageProcessor {
    private static final String ERROR_PROCESSING_LINK_ACTION = "Error processing link action";
    private static final String ERROR_MESSAGE = "An error occurred. Please try again later.";
    private final CommandHolder commands;
    private final LinkService linkService;
    private final BotStateService botStateService;

    @Override
    public SendMessage process(Update update) {
        log.info("Processing update: {}", update.message().text());
        BotState currentState = botStateService.get();

        return switch (currentState) {
            case WAITING_FOR_LINK_SAVE -> handleSaveLink(update);
            case WAITING_FOR_LINK_REMOVE -> handleRemoveLink(update);
            default -> handleCommandOrUnknown(update);
        };
    }

    private SendMessage handleSaveLink(Update update) {
        Long chatId = update.message().chat().id();
        Long userId = update.message().from().id();
        String link = update.message().text();

        try {
            if (linkService.checkLinkFormat(link)) {
                linkService.save(userId, link);
                botStateService.reset();
                return new SendMessage(chatId, "Link saved.");
            } else {
                return MessageUtils.createInvalidLinkResponse(update);
            }
        } catch (Exception e) {
            log.error(ERROR_PROCESSING_LINK_ACTION, e);
            return new SendMessage(chatId, ERROR_MESSAGE);
        }
    }

    private SendMessage handleRemoveLink(Update update) {
        Long chatId = update.message().chat().id();
        Long userId = update.message().from().id();
        String link = update.message().text();

        try {
            if (linkService.exists(userId, link)) {
                linkService.remove(userId, link);
                botStateService.reset();
                return new SendMessage(chatId, "Link removed.");
            } else {
                return new SendMessage(chatId, "Invalid link. Please provide a valid link to remove.");
            }
        } catch (Exception e) {
            log.error(ERROR_PROCESSING_LINK_ACTION, e);
            return new SendMessage(chatId, ERROR_MESSAGE);
        }
    }

    private SendMessage handleCommandOrUnknown(Update update) {
        Command command = commands.get(update.message().text());
        return command != null && command.supports(update)
            ? command.handle(update)
            : MessageUtils.createUnknownCommandResponse(update);
    }
}
