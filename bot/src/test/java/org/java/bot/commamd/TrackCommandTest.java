package org.java.bot.commamd;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.java.bot.command.TrackCommand;
import org.java.bot.service.BotStateService;
import org.java.bot.utils.BotState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrackCommandTest {

    @InjectMocks
    private TrackCommand trackCommand;

    @Mock
    private BotStateService botStateService;

    @Mock
    private Update update;

    @Test
    public void testCommand() {
        assertEquals("/track", trackCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("start tracking a link", trackCommand.description());
    }

    @Test
    void testHandle() {
        setupUpdateMock(123L);

        SendMessage response = trackCommand.handle(update);
        verify(botStateService).set(BotState.WAITING_FOR_LINK_SAVE);

        assertEquals(response.getParameters().get("text"), "Enter tracking link:");
    }

    private void setupUpdateMock(Long chatId) {
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(chatId);
    }
}
