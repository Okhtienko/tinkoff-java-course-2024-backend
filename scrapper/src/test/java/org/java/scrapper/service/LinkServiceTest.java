package org.java.scrapper.service;

import org.java.scrapper.IntegrationTest;
import org.java.scrapper.converter.LinkConverter;
import org.java.scrapper.dto.link.LinkRequest;
import org.java.scrapper.dto.link.LinkResponse;
import org.java.scrapper.exception.ConflictException;
import org.java.scrapper.exception.NotFoundException;
import org.java.scrapper.jdbc.JdbcLinkRepository;
import org.java.scrapper.model.Link;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LinkServiceTest extends IntegrationTest {
    @Autowired
    private LinkService linkService;

    @MockBean
    private JdbcLinkRepository jdbcLinkRepository;

    @MockBean
    private LinkConverter converter;

    @Test
    @Transactional
    @Rollback
    public void testSaveSuccess() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        Link link = buildLink(1L, url, createdBy);
        LinkRequest request = buildLinkRequest(url, createdBy);
        LinkResponse expectedResponse = buildLinkResponse(1L, url, createdBy);

        when(jdbcLinkRepository.existsByUrlAndChatId(url, chatId)).thenReturn(false);
        when(jdbcLinkRepository.existsByUrl(url)).thenReturn(false);
        when(jdbcLinkRepository.save(url, createdBy, chatId)).thenReturn(link);
        when(converter.toDto(link)).thenReturn(expectedResponse);

        LinkResponse response = linkService.save(request, chatId);

        verify(jdbcLinkRepository).existsByUrlAndChatId(url, chatId);
        verify(jdbcLinkRepository).existsByUrl(url);
        verify(jdbcLinkRepository).save(url, createdBy, chatId);
        verify(converter).toDto(link);

        assertEquals(expectedResponse, response);
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveSuccessExistingLink() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        Link link = buildLink(1L, url, createdBy);
        LinkRequest request = buildLinkRequest(url, createdBy);
        LinkResponse expectedResponse = buildLinkResponse(1L, url, createdBy);

        when(jdbcLinkRepository.existsByUrlAndChatId(url, chatId)).thenReturn(false);
        when(jdbcLinkRepository.existsByUrl(url)).thenReturn(true);
        when(jdbcLinkRepository.get(url)).thenReturn(link);
        when(converter.toDto(link)).thenReturn(expectedResponse);

        LinkResponse response = linkService.save(request, chatId);

        verify(jdbcLinkRepository).existsByUrlAndChatId(url, chatId);
        verify(jdbcLinkRepository).existsByUrl(url);
        verify(jdbcLinkRepository).get(url);
        verify(jdbcLinkRepository).updateLinkChat(link.getId(), chatId);
        verify(converter).toDto(link);

        assertEquals(expectedResponse, response);
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveConflict() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        LinkRequest request = buildLinkRequest(url, createdBy);

        when(jdbcLinkRepository.existsByUrlAndChatId(url, chatId)).thenReturn(true);
        assertThrows(ConflictException.class, () -> linkService.save(request, chatId));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveSuccess() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        Link link = buildLink(1L, url, createdBy);
        LinkRequest request = buildLinkRequest(url, createdBy);
        LinkResponse expectedResponse = buildLinkResponse(1L, url, createdBy);

        when(jdbcLinkRepository.existsByUrlAndChatId(url, chatId)).thenReturn(true);
        when(jdbcLinkRepository.get(url, chatId)).thenReturn(link);
        when(jdbcLinkRepository.existsLinkChat(link.getId())).thenReturn(false);
        when(converter.toDto(link)).thenReturn(expectedResponse);

        LinkResponse response = linkService.remove(request, chatId);

        verify(jdbcLinkRepository).existsByUrlAndChatId(url, chatId);
        verify(jdbcLinkRepository).get(url, chatId);
        verify(jdbcLinkRepository).removeLinkChat(link.getId(), chatId);
        verify(jdbcLinkRepository).remove(url);

        assertEquals(url, response.getUrl().toString());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveNotFound() {
        String url = "https://www.example.com";
        Long chatId = 123L;

        when(jdbcLinkRepository.existsByUrlAndChatId(url, chatId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> linkService.remove(new LinkRequest().setUrl(url), chatId));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetSuccess() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        LinkRequest request =buildLinkRequest(url, createdBy);
        Link link = buildLink(1L, url, createdBy);
        LinkResponse expectedResponse = buildLinkResponse(1L, url, createdBy);

        when(jdbcLinkRepository.existsByUrlAndChatId(url, chatId)).thenReturn(true);
        when(jdbcLinkRepository.get(url, chatId)).thenReturn(link);
        when(converter.toDto(link)).thenReturn(expectedResponse);

        LinkResponse response = linkService.get(request, chatId);

        assertEquals(expectedResponse, response);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetNotFound() {
        String url = "https://www.example.com";
        Long chatId = 123L;

        when(jdbcLinkRepository.existsByUrlAndChatId(url, chatId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> linkService.get(new LinkRequest().setUrl(url), chatId));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsSuccess() {
        Long chatId = 123L;

        List<Link> links = List.of(
            buildLink(1L,  "https://www.example.com", "User"),
            buildLink(2L,  "https://www.example.com", "User")
        );

        List<LinkResponse> expectedResponses = List.of(
            buildLinkResponse(1L,  "https://www.example.com", "User"),
            buildLinkResponse(2L,  "https://www.example.com", "User")
        );

        when(jdbcLinkRepository.gets(chatId)).thenReturn(links);
        when(converter.toDto(links)).thenReturn(expectedResponses);

        List<LinkResponse> responses = linkService.gets(chatId);

        assertEquals(expectedResponses, responses);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsEmpty() {
        Long chatId = 123L;
        List<Link> links = new ArrayList<>();
        List<LinkResponse> expectedResponses = new ArrayList<>();

        when(jdbcLinkRepository.gets(chatId)).thenReturn(links);
        when(converter.toDto(links)).thenReturn(expectedResponses);

        List<LinkResponse> responses = linkService.gets(123L);

        assertTrue(responses.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsByLastCheck() {
        Long delay = 300L;
        List<Link> links = List.of(
            buildLink(1L,  "https://www.example.com", "User"),
            buildLink(2L,  "https://www.example.com", "User")
        );

        List<LinkResponse> expectedResponses = List.of(
            buildLinkResponse(1L,  "https://www.example.com", "User"),
            buildLinkResponse(1L,  "https://www.example.com", "User")
        );

        when(jdbcLinkRepository.getsByLastCheck(delay)).thenReturn(links);
        when(converter.toDto(links)).thenReturn(expectedResponses);

        List<LinkResponse> responses = linkService.getsByLastCheck(delay);

        assertEquals(expectedResponses, responses);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsChatByLastCheckSuccess() {
        Long delay = 300L;
        String url = "https://www.example.com";
        LinkResponse link = buildLinkResponse(1L,  "https://www.example.com", "User");

        List<Long> expectedChats = List.of(1L, 2L);

        when(jdbcLinkRepository.existsByUrl(url)).thenReturn(true);
        when(jdbcLinkRepository.getsChatByLastCheck(delay, url)).thenReturn(expectedChats);

        List<Long> chats = linkService.getsChatByLastCheck(delay, link);

        assertEquals(expectedChats, chats);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsChatByLastCheckNotFound() {
        Long delay = 300L;
        String url = "https://www.example.com";
        LinkResponse link = buildLinkResponse(1L,  "https://www.example.com", "User");

        when(jdbcLinkRepository.existsByUrl(url)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> linkService.getsChatByLastCheck(delay, link));
    }

    @Test
    public void testUpdateLastCheck() {
        LinkResponse response = buildLinkResponse(1L,  "https://www.example.com", "User");
        OffsetDateTime date = OffsetDateTime.now();

        linkService.updateLastCheck(response, date);

        verify(jdbcLinkRepository).updateLastCheck(response.getId(), date);
    }

    private Link buildLink(Long id, String url, String createdBy) {
        return Link.builder()
            .id(id)
            .url(url)
            .createdBy(createdBy)
            .lastCheck(OffsetDateTime.now())
            .createdAt(OffsetDateTime.now())
            .build();
    }

    private LinkResponse buildLinkResponse(Long id, String url, String createdBy) {
        return new LinkResponse().setId(id)
            .setUrl(URI.create(url))
            .setCreatedBy(createdBy)
            .setLastCheck(OffsetDateTime.now());
    }

    private LinkRequest buildLinkRequest(String url, String createdBy) {
        return new LinkRequest().setUrl(url).setCreatedBy(createdBy);
    }
}
