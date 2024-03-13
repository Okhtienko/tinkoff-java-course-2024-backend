package org.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.aspect.BotCommand;
import org.java.bot.bot.Command;
import org.java.bot.service.LinkService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private static final String COMMAND = "/list";
    private static final String LIST_DESCRIPTION = "show list of tracked links";
    private final LinkService linkService;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return LIST_DESCRIPTION;
    }

    @BotCommand
    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        Long userId = update.message().from().id();
        log.info("Processing list command for chat ID: {}", chatId);

        List<String> links = linkService.get(userId);
        String message = getMessageContent(links);

        log.info("Sending list message to chat ID: {}", chatId);
        return new SendMessage(chatId, message);
    }

    private String getMessageContent(List<String> links) {
        return links == null || links.isEmpty()
            ? "List of links is empty"
            : "List of tracked links:\n\n"
            + links.stream()
            .map(link -> "- " + link)
            .collect(Collectors.joining("\n"));
    }
}
