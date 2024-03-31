package org.java.scrapper.jdbc;

import org.java.scrapper.model.Link;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
public class JdbcLinkRepositoryTest {
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Test
    @Transactional
    @Rollback
    public void testSaveSuccess() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        Link link = jdbcLinkRepository.save(url, createdBy, chatId);

        assertNotNull(link.getId());
        assertEquals(url, link.getUrl());
        assertEquals(createdBy, link.getCreatedBy());
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveNullUrl() {
        String createdBy = "User";
        assertThrows(DataIntegrityViolationException.class,
            () -> jdbcLinkRepository.save(null, createdBy, 123L));
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveNullChatId() {
        String url = "https://www.example.com";
        String createdBy = "User";
        assertThrows(DataIntegrityViolationException.class,
            () -> jdbcLinkRepository.save(url, createdBy, null));
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveNull() {
        assertThrows(DataIntegrityViolationException.class, () ->
            jdbcLinkRepository.save(null, null, null)
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveNullCreatedBy() {
        String url = "https://www.example.com";
        assertThrows(DataIntegrityViolationException.class,
            () -> jdbcLinkRepository.save(url, null, 123L));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetByUrlSuccess() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        jdbcLinkRepository.save(url, createdBy, chatId);
        Link link = jdbcLinkRepository.get(url);

        assertEquals(url, link.getUrl());
        assertEquals(createdBy, link.getCreatedBy());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetByUrlException() {
        assertThrows(EmptyResultDataAccessException.class, () -> jdbcLinkRepository.get("https://www.example.com"));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetByChatIdAndUrlSuccess() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        jdbcLinkRepository.save(url, createdBy, chatId);

        Link link = jdbcLinkRepository.get(url, chatId);

        assertNotNull(link);
        assertEquals(url, link.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetByChatIdAndUrlException() {
        Long chatId = 123L;
        String url = "https://www.example.com";

        assertThrows(EmptyResultDataAccessException.class, () -> jdbcLinkRepository.get(url, chatId));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveSuccess() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        jdbcLinkRepository.save(url, createdBy, chatId);
        Link link = jdbcLinkRepository.get(url);

        jdbcLinkRepository.removeLinkChat(link.getId(), chatId);
        jdbcLinkRepository.remove(url);

        assertNotNull(link);
        assertEquals(url, link.getUrl());
        assertFalse(jdbcLinkRepository.existsByUrl(url));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveException() {
        String url = "https://example.com";
        assertThrows(EmptyResultDataAccessException.class, () -> jdbcLinkRepository.remove(url));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsSuccessWithLinks() {
        jdbcLinkRepository.save("https://example.com/1", "User", 123L);
        jdbcLinkRepository.save("https://example.com/2", "User", 123L);

        List<Link> links = jdbcLinkRepository.gets(123L);

        assertNotNull(links);
        assertEquals(2, links.size());
        assertTrue(links.stream().anyMatch(link -> link.getUrl().equals("https://example.com/1")));
        assertTrue(links.stream().anyMatch(link -> link.getUrl().equals("https://example.com/2")));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsEmpty() {
        Long chatId = 123L;
        List<Link> links = jdbcLinkRepository.gets(chatId);
        assertTrue(links.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsByLastCheckSuccess() {
        Long delay = 5L;

        jdbcLinkRepository.save("https://example.com/1", "User", 123L);
        jdbcLinkRepository.save("https://example.com/2", "User", 123L);

        List<Link> links = jdbcLinkRepository.getsByLastCheck(delay);

        assertNotNull(links);
        assertEquals(1, links.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsByLastCheckNullDelay() {
        assertThrows(NullPointerException.class, () -> jdbcLinkRepository.getsByLastCheck(null));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsChatByLastCheckNullDelay() {
        String url = "https://example.com";
        assertThrows(NullPointerException.class, () -> jdbcLinkRepository.getsChatByLastCheck(null, url));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsChatByLastCheckNoLink() {
        Long delay = 5L;
        String url = "https://example.com";

        List<Long> chats = jdbcLinkRepository.getsChatByLastCheck(delay, url);

        assertNotNull(chats);
        assertTrue(chats.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsByUrlSuccess() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        jdbcLinkRepository.save(url, createdBy, chatId);

        assertTrue(jdbcLinkRepository.existsByUrl(url));
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsByUrlNotFound() {
        String url = "https://www.example.com";
        assertFalse(jdbcLinkRepository.existsByUrl(url));
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsByUrlAndChatIdSuccess() {
        Long chatId = 123L;
        String url = "https://www.example.com";
        String createdBy = "User";

        jdbcLinkRepository.save(url, createdBy, chatId);

        assertTrue(jdbcLinkRepository.existsByUrlAndChatId(url, chatId));
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsByUrlAndChatIdNotFoundUrl() {
        Long chatId = 123L;
        String url = "https://not-existent.com";
        String createdBy = "User";

        jdbcLinkRepository.save("https://another-example.com", createdBy, chatId);

        assertFalse(jdbcLinkRepository.existsByUrlAndChatId(url, chatId));
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsByUrlAndChatIdNotFoundChatId() {
        String url = "https://example.com";
        Long chatId = 456L;
        String createdBy = "User";

        jdbcLinkRepository.save(url, createdBy, 123L);

        assertFalse(jdbcLinkRepository.existsByUrlAndChatId(url, chatId));
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsLinkChatSuccess() {
        Long chatId = 123L;
        String url = "https://example.com";
        String createdBy = "User";

        Link link = jdbcLinkRepository.save(url, createdBy, chatId);

        assertTrue(jdbcLinkRepository.existsLinkChat(link.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsLinkChatNotFound() {
        assertFalse(jdbcLinkRepository.existsLinkChat(456L));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateLinkChatSuccess() {
        Long chatId = 123L;
        String url = "https://example.com";
        String createdBy = "User";

        Link link = jdbcLinkRepository.save(url, createdBy, chatId);

        jdbcLinkRepository.updateLinkChat(link.getId(), 789L);

        assertTrue(jdbcLinkRepository.existsByUrlAndChatId(url, 789L));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateLinkChatNullChatId() {
        Long chatId = 123L;
        String url = "https://example.com";
        String createdBy = "User";

        Link link = jdbcLinkRepository.save(url, createdBy, chatId);

        assertThrows(DataIntegrityViolationException.class, () ->
            jdbcLinkRepository.updateLinkChat(link.getId(), null)
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateLinkChatNullId() {
        Long chatId = 123L;
        assertThrows(DataIntegrityViolationException.class, () ->
            jdbcLinkRepository.updateLinkChat(null, chatId)
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkChatSuccess() {
        Long chatId = 123L;
        String url = "https://example.com";
        String createdBy = "User";

        Link link = jdbcLinkRepository.save(url, createdBy, chatId);

        jdbcLinkRepository.removeLinkChat(link.getId(), chatId);

        assertFalse(jdbcLinkRepository.existsLinkChat(link.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkChatException() {
        Long chatId = 123L;
        String url = "https://example.com";
        String createdBy = "User";

        Link link = jdbcLinkRepository.save(url, createdBy, chatId);

        jdbcLinkRepository.removeLinkChat(link.getId(), 456L);
        assertTrue(jdbcLinkRepository.existsLinkChat(link.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateLastCheckSuccess() {
        Long chatId = 123L;
        String url = "https://example.com";
        String createdBy = "User";

        OffsetDateTime date = OffsetDateTime.now(ZoneOffset.UTC);

        Link link = jdbcLinkRepository.save(url, createdBy, chatId);

        jdbcLinkRepository.updateLastCheck(link.getId(), date);

        Link updatedLink = jdbcLinkRepository.get(url);
        assertEquals(date.toLocalDate(), updatedLink.getLastCheck().toLocalDate());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateLastCheckInvalidDate() {
        Long chatId = 123L;
        String url = "https://example.com";
        String createdBy = "User";

        Link link = jdbcLinkRepository.save(url, createdBy, chatId);

        assertThrows(NullPointerException.class, () ->
            jdbcLinkRepository.updateLastCheck(link.getId(), null)
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateLastCheckMultipleChats() {
        String url = "https://example.com";
        String createdBy = "User";

        Link link = jdbcLinkRepository.save(url, createdBy, 123L);
        jdbcLinkRepository.updateLinkChat(link.getId(), 789L);

        OffsetDateTime date = OffsetDateTime.now();
        jdbcLinkRepository.updateLastCheck(link.getId(), date);

        Link linkFromFirstChat = jdbcLinkRepository.get(url, 123L);
        Link linkFromSecondChat = jdbcLinkRepository.get(url, 789L);

        assertEquals(date.toLocalDate(), linkFromFirstChat.getLastCheck().toLocalDate());
        assertEquals(date.toLocalDate(), linkFromSecondChat.getLastCheck().toLocalDate());
    }
}
