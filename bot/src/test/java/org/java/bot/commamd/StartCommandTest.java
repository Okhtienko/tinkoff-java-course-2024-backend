package org.java.bot.commamd;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.java.bot.command.StartCommand;
import org.java.bot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StartCommandTest {
    @InjectMocks
    private StartCommand startCommand;

    @Mock
    private UserService userService;

    @Mock
    private Update update;

    @Test
    public void testCommand() {
        assertEquals("/start", startCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("register a user", startCommand.description());
    }

    @Test
    public void testHandleForExistingUser() {
        setupUpdateMock(123L, 456L);

        when(userService.exists(anyLong())).thenReturn(true);
        SendMessage response = startCommand.handle(update);

        String expectedResponse = "This user is already registered and has access to all bot features. " +
            "Type /help to view the list of commands.";
        assertEquals(expectedResponse, response.getParameters().get("text"));
    }

    @Test
    void testHandleForNewUser() {
        setupUpdateMock(123L, 456L);

        when(userService.exists(anyLong())).thenReturn(false);
        SendMessage response = startCommand.handle(update);

        String expectedResponse = "Welcome! You are now registered and have access to all bot features. " +
            "Type /help to view the list of commands.";;
        assertEquals(expectedResponse, response.getParameters().get("text"));
    }

    private void setupUpdateMock(Long chatId, Long userId) {
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().from()).thenReturn(mock(User.class));
        when(update.message().from().id()).thenReturn(userId);
    }
}
