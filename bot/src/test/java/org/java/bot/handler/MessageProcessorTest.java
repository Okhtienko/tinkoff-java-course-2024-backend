package org.java.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.java.bot.component.CommandHolder;
import org.java.bot.service.BotStateService;
import org.java.bot.service.LinkService;
import org.java.bot.utils.BotState;
import org.java.bot.utils.MessageUtils;
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
public class MessageProcessorTest {
    @Mock
    private Update update;

    @Mock
    private BotStateService botStateService;

    @Mock
    private CommandHolder commands;

    @Mock
    private LinkService linkService;

    @InjectMocks
    private MessageProcessor messageProcessor;

    @Test
    void testProcessSaveLinkSuccess() {
        Long chatId = 123L;
        Long userId = 456L;
        String link = "https://example.com";
        BotState state = BotState.WAITING_FOR_LINK_SAVE;

        setupUpdateMock(chatId, userId, link);
        when(botStateService.get()).thenReturn(state);
        when(linkService.check(link)).thenReturn(true);

        SendMessage response = messageProcessor.process(update);
        verify(botStateService).reset();
        verify(linkService).save(userId, link);
        assertEquals(response.getParameters().get("text"), "Link saved.");
    }

    @Test
    void testProcessRemoveLinkSuccess() {
        Long chatId = 123L;
        Long userId = 456L;
        String link = "https://example.com";
        BotState state = BotState.WAITING_FOR_LINK_REMOVE;


        setupUpdateMock(chatId, userId, link);
        when(botStateService.get()).thenReturn(state);
        when(linkService.exists(userId, link)).thenReturn(true);

        SendMessage response = messageProcessor.process(update);
        verify(botStateService).reset();
        verify(linkService).remove(userId, link);
        assertEquals(response.getParameters().get("text"), "Link removed.");
    }

    @Test
    void testProcessUnknownCommand() {
        Long chatId = 123L;
        String unknownCommand = "unknown command";

        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().text()).thenReturn(unknownCommand);
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(chatId);
        when(botStateService.get()).thenReturn(BotState.WAITING_COMMAND);

        SendMessage response = messageProcessor.process(update);
        verify(commands).get(unknownCommand);
        SendMessage expectedResponse = MessageUtils.createUnknownCommandResponse(update);
        assertEquals(response.getParameters().get("text"), expectedResponse.getParameters().get("text"));
    }

    @Test
    public void testProcessSaveLinkInvalidLink() {
        Long chatId = 123L;
        Long userId = 456L;
        String link = "invalid link";
        BotState state = BotState.WAITING_FOR_LINK_SAVE;

        setupUpdateMock(chatId, userId, link);
        when(botStateService.get()).thenReturn(state);
        when(linkService.check(link)).thenReturn(false);

        SendMessage response = messageProcessor.process(update);
        SendMessage expectedResponse = MessageUtils.createInvalidLinkResponse(update);
        assertEquals(response.getParameters().get("text"), expectedResponse.getParameters().get("text"));
    }

    @Test
    public void testProcessRemoveLinkNonExistentLink() {
        Long chatId = 123L;
        Long userId = 456L;
        String link = "https://example.com";
        BotState state = BotState.WAITING_FOR_LINK_REMOVE;

        setupUpdateMock(chatId, userId, link);
        when(botStateService.get()).thenReturn(state);
        when(linkService.exists(userId, link)).thenReturn(false);

        SendMessage response = messageProcessor.process(update);
        SendMessage expectedResponse = MessageUtils.createInvalidLinkResponse(update);
        assertEquals(response.getParameters().get("text"), expectedResponse.getParameters().get("text"));
    }

    private void setupUpdateMock(Long chatId, Long userId, String message) {
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().text()).thenReturn(message);
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().from()).thenReturn(mock(User.class));
        when(update.message().from().id()).thenReturn(userId);
    }
}
