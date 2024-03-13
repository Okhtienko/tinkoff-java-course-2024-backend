package org.java.bot.commamd;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.java.bot.command.UntrackCommand;
import org.java.bot.service.BotStateService;
import org.java.bot.service.LinkService;
import org.java.bot.utils.BotState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UntrackCommandTest {

    @InjectMocks
    private UntrackCommand untrackCommand;

    @Mock
    private BotStateService botStateService;

    @Mock
    private LinkService linkService;

    @Mock
    private Update update;

    @Test
    public void testCommand() {
        assertEquals("/untrack", untrackCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("stop tracking a link", untrackCommand.description());
    }

    @Test
    public void testHandleWithNoTrackedLinks() {
        setupUpdateMock(123L, 456L);

        when(linkService.get(anyLong())).thenReturn(Collections.emptyList());

        SendMessage response = untrackCommand.handle(update);
        assertEquals(response.getParameters().get("text"), "There are no tracked links currently.");
    }

    @Test
    void testHandleWithTrackedLinks() {
        setupUpdateMock(123L, 456L);
        List<String> links = List.of(
            "https://github.com/sanyarnd/tinkoff-java-course-2023/",
            "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
            "https://stackoverflow.com/search?q=unsupported%20link"
        );

        when(linkService.get(anyLong())).thenReturn(links);

        SendMessage response = untrackCommand.handle(update);
        verify(botStateService).set(BotState.WAITING_FOR_LINK_REMOVE);
        assertEquals(response.getParameters().get("text"), "Enter untracking link:");
    }

    private void setupUpdateMock(Long chatId, Long userId) {
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().from()).thenReturn(mock(User.class));
        when(update.message().from().id()).thenReturn(userId);
    }
}
