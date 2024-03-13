package org.java.bot.commamd;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.java.bot.command.ListCommand;
import org.java.bot.service.LinkService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {

    @InjectMocks
    private ListCommand listCommand;

    @Mock
    private LinkService linkService;

    @Mock
    private Update update;

    @Test
    public void testCommand() {
        assertEquals("/list", listCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("show list of tracked links", listCommand.description());
    }

    @Test
    public void testHandleCommandWithEmptyLinks() {
        setupUpdateMock(123L, 456L);
        when(linkService.get(anyLong())).thenReturn(Collections.emptyList());

        SendMessage response = listCommand.handle(update);
        assertEquals(response.getParameters().get("text"), "List of links is empty");
    }

    @Test
    public void testHandleCommand() {
        List<String> links = List.of(
            "https://github.com/sanyarnd/tinkoff-java-course-2023/",
            "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
            "https://stackoverflow.com/search?q=unsupported%20link"
        );

        String expectedResponse = "List of tracked links:\n" + "\n" +
            "- https://github.com/sanyarnd/tinkoff-java-course-2023/\n" +
            "- https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c\n" +
            "- https://stackoverflow.com/search?q=unsupported%20link";

        setupUpdateMock(1234L, 456L);
        when(linkService.get(anyLong())).thenReturn(links);

        SendMessage response = listCommand.handle(update);
        assertEquals(response.getParameters().get("text"), expectedResponse);
    }

    private void setupUpdateMock(Long chatId, Long userId) {
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().from()).thenReturn(mock(User.class));
        when(update.message().from().id()).thenReturn(userId);
    }
}
