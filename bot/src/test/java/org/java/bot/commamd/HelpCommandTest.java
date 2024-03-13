package org.java.bot.commamd;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.java.bot.bot.Command;
import org.java.bot.command.HelpCommand;
import org.java.bot.command.ListCommand;
import org.java.bot.command.StartCommand;
import org.java.bot.component.CommandHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HelpCommandTest {

    @InjectMocks
    private HelpCommand helpCommand;

    @Mock
    private CommandHolder commandHolder;

    @Mock
    private Update update;

    @Test
    public void testCommand() {
        assertEquals("/help", helpCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("display help menu", helpCommand.description());
    }

    @Test
    public void testHandleCommands() {
        setupUpdateMock(123L);
        Map<String, Command> commands = new HashMap<>();
        commands.put("/start", new StartCommand(null));
        commands.put("/list", new ListCommand(null));

        when(commandHolder.gets()).thenReturn(commands);

        SendMessage response = helpCommand.handle(update);
        String expectedResponse = "Available commands:\n\n" +
            "/list - show list of tracked links\n" +
            "/start - register a user";
        assertEquals(response.getParameters().get("text"), expectedResponse);
    }

    private void setupUpdateMock(Long chatId) {
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(chatId);
    }
}
