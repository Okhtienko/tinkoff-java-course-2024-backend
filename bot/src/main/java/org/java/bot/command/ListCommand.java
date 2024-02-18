package org.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.aspect.BotCommand;
import org.java.bot.bot.Command;
import org.java.bot.service.UserService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private static final String COMMAND = "/list";
    private static final String LIST_DESCRIPTION = "show list of tracked links";
    private final UserService userService;

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
        log.info("Processing list command for chat ID: {}", chatId);

        List<String> trackedLinks = userService.links(update.message().from().id());
        String message = getMessageContent(trackedLinks);

        log.info("Sending list message to chat ID: {}", chatId);
        return new SendMessage(chatId, message);
    }

    private String getMessageContent(List<String> trackedLinks) {
        return trackedLinks.isEmpty()
            ? "List of links is empty"
            : "List of tracked links:\n\n"
            + trackedLinks.stream()
            .map(link -> "- " + link)
            .collect(Collectors.joining("\n"));
    }
}
