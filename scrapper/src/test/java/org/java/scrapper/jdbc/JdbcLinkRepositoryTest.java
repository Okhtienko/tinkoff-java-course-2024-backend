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
        String url = "https://www.example.com";
        String createdBy = "User";
        Link link = jdbcLinkRepository.save(url, createdBy, 123L);
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
    public void testGetsSuccess() {
        List<Link> links = jdbcLinkRepository.gets(123L);
        assertEquals(links.size(), 2);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsEmpty() {
        List<Link> links = jdbcLinkRepository.gets(456L);
        assertTrue(links.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsSuccess() {
        Link link = jdbcLinkRepository.save("https://www.example.com", "User", 123L);
        assertTrue(jdbcLinkRepository.exists(link.getUrl()));
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsFalse() {
        assertFalse(jdbcLinkRepository.exists("https://www.example.com"));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetSuccess() {
        String url = "https://www.example.com";
        String createdBy = "User";

        Link savedLink = jdbcLinkRepository.save(url, createdBy, 123L);
        Link link = jdbcLinkRepository.get(url, 123L);

        assertEquals(savedLink.getId(), link.getId());
        assertEquals(savedLink.getUrl(), link.getUrl());
        assertEquals(savedLink.getCreatedBy(), link.getCreatedBy());
        assertEquals(savedLink.getLastCheck(), link.getLastCheck());
        assertEquals(savedLink.getCreatedAt(), link.getCreatedAt());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetException() {
        assertThrows(EmptyResultDataAccessException.class,
            () -> jdbcLinkRepository.get("https://www.example.com", 456L)
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveSuccess() {
        String url = "https://www.example.com";
        String createdBy = "User";

        Link savedLink = jdbcLinkRepository.save(url, createdBy, 123L);
        Link removedLink = jdbcLinkRepository.remove(savedLink.getUrl(), 123L);

        assertEquals(savedLink.getId(), removedLink.getId());
        assertEquals(savedLink.getUrl(), removedLink.getUrl());
        assertEquals(savedLink.getCreatedBy(), removedLink.getCreatedBy());
        assertEquals(savedLink.getLastCheck(), removedLink.getLastCheck());
        assertEquals(savedLink.getCreatedAt(), removedLink.getCreatedAt());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsByLastCheckEmpty() {
        List<Link> links = jdbcLinkRepository.getsByLastCheck();
        assertNotNull(links);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsChatByLastCheckEmpty() {
        List<Long> chats = jdbcLinkRepository.getsChatByLastCheck("https://www.example.com");
        assertNotNull(chats);
    }
}
