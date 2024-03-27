package org.java.scrapper.service;

import org.java.scrapper.IntegrationTest;
import org.java.scrapper.converter.ChatConverter;
import org.java.scrapper.dto.chat.ChatRequest;
import org.java.scrapper.dto.chat.ChatResponse;
import org.java.scrapper.exception.ConflictException;
import org.java.scrapper.exception.NotFoundException;
import org.java.scrapper.jdbc.JdbcChatRepository;
import org.java.scrapper.model.Chat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ChatServiceTest extends IntegrationTest {
    @Autowired
    private ChatService chatService;

    @MockBean
    private JdbcChatRepository jdbcChatRepository;

    @MockBean
    private ChatConverter converter;

    @Test
    @Transactional
    @Rollback
    public void testSaveSuccess() {
        Long id = 123L;
        ChatRequest request = buildChatRequest(id, "Chat", "User");

        when(jdbcChatRepository.exists(id)).thenReturn(false);
        chatService.save(request);

        verify(jdbcChatRepository).save(id, request.getName(), request.getCreatedBy());
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveConflict() {
        Long id = 123L;
        ChatRequest request = buildChatRequest(id, "Chat", "User");

        when(jdbcChatRepository.exists(id)).thenReturn(true);

        assertThrows(ConflictException.class, () -> chatService.save(request));
    }

    @Test
    public void testGetSuccess() {
        Long id = 123L;
        Chat chat = buildChat(id, "Chat", "User");
        ChatResponse expectedResponse = buildChatResponse(id, "Chat", "User");

        when(jdbcChatRepository.exists(id)).thenReturn(true);
        when(jdbcChatRepository.get(id)).thenReturn(chat);
        when(converter.toDto(chat)).thenReturn(expectedResponse);

        ChatResponse response = chatService.get(id);
        assertEquals(expectedResponse, response);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetNotFound() {
        Long id = 123L;
        when(jdbcChatRepository.exists(id)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> chatService.get(id));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsSuccess() {
        List<Chat> chats = List.of(
            buildChat(123L, "Chat", "User"),
            buildChat(456L, "Chat", "User")
        );

        List<ChatResponse> expectedResponses = List.of(
            buildChatResponse(123L, "Chat", "User"),
            buildChatResponse(456L, "Chat", "User")
        );

        when(jdbcChatRepository.gets()).thenReturn(chats);
        when(converter.toDto(chats)).thenReturn(expectedResponses);

        List<ChatResponse> responses = chatService.gets();

        assertEquals(expectedResponses, responses);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsEmpty() {
        List<Chat> chats = new ArrayList<>();
        List<ChatResponse> expectedResponses = new ArrayList<>();

        when(jdbcChatRepository.gets()).thenReturn(chats);
        when(converter.toDto(chats)).thenReturn(expectedResponses);

        List<ChatResponse> responses = chatService.gets();

        assertTrue(responses.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteSuccess() {
        Long id = 123L;

        when(jdbcChatRepository.exists(id)).thenReturn(true);
        chatService.delete(id);

        verify(jdbcChatRepository).delete(id);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteNotFound() {
        Long id = 123L;
        when(jdbcChatRepository.exists(id)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> chatService.delete(id));
    }

    private Chat buildChat(Long id, String name, String createBy) {
        return Chat.builder().id(id).name(name).createdBy(createBy).build();
    }
    private ChatRequest buildChatRequest(Long id, String name, String createdBy) {
        return new ChatRequest().setId(id).setName(name).setCreatedBy(createdBy);
    }

    private ChatResponse buildChatResponse(Long id, String name, String createdBy) {
        return new ChatResponse().setId(id).setName(name).setCreatedBy(createdBy);
    }
}
