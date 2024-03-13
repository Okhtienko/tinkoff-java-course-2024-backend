package org.java.bot.utils;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MessageUtils {

    private MessageUtils() {}

    public static SendMessage createInvalidLinkResponse(Update update) {
        Long chatId = update.message().chat().id();
        String message = "Invalid link. Please try again.";
        return new SendMessage(chatId, message);
    }

    public static SendMessage createUnknownCommandResponse(Update update) {
        Long chatId = update.message().chat().id();
        String message = update.message().text();
        log.info("Sending unknown command response: {}", update.message().text());
        return new SendMessage(chatId, message + " - unknown command");
    }
}
