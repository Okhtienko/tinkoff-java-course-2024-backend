package org.java.scrapper.jdbc;

import org.java.scrapper.IntegrationTest;
import org.java.scrapper.model.Chat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Test
    @Transactional
    @Rollback
    public void testSaveSuccess() {
        Long id = 123L;
        String name = "Chat";
        String createdBy = "User";

        jdbcChatRepository.save(id, name, createdBy);

        Chat chat = jdbcChatRepository.get(id);

        assertEquals(id, chat.getId());
        assertEquals(name, chat.getName());
        assertEquals(createdBy, chat.getCreatedBy());
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveNullIdException() {
        String name = "Chat";
        String createdBy = "User";
        assertThrows(DataIntegrityViolationException.class, () -> jdbcChatRepository.save(null, name, createdBy));
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveNullNameException() {
        Long id = 123L;
        String createdBy = "User";
        assertThrows(DataIntegrityViolationException.class, () -> jdbcChatRepository.save(id, null, createdBy));
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveNullCreatedByException() {
        Long id = 123L;
        String name = "Chat";
        assertThrows(DataIntegrityViolationException.class, () -> jdbcChatRepository.save(id, name, null));
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveNullException() {
        assertThrows(DataIntegrityViolationException.class, () ->
            jdbcChatRepository.save(null, null, null)
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsSuccess() {
        Long id = 123L;
        String name = "Chat";
        String createdBy = "User";

        jdbcChatRepository.save(id, name, createdBy);
        assertTrue(jdbcChatRepository.exists(id));
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsFalse() {
        Long id = 456L;
        assertFalse(jdbcChatRepository.exists(id));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetSuccess() {
        Long id = 123L;
        String name = "Chat";
        String createdBy = "User";

        jdbcChatRepository.save(id, name, createdBy);

        Chat chat = jdbcChatRepository.get(id);
        assertEquals(id, chat.getId());
        assertEquals(name, chat.getName());
        assertEquals(createdBy, chat.getCreatedBy());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetException() {
        assertThrows(EmptyResultDataAccessException.class, () -> jdbcChatRepository.get(456L));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteSuccess() {
        Long id = 123L;
        String name = "Chat";
        String createdBy = "User";

        jdbcChatRepository.save(id, name, createdBy);
        jdbcChatRepository.delete(id);

        assertFalse(jdbcChatRepository.exists(id));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsEmpty() {
        List<Chat> chats = jdbcChatRepository.gets();
        assertTrue(chats.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetsSuccess() {
        jdbcChatRepository.save(123L, "Chat", "User");
        jdbcChatRepository.save(456L, "Chat", "User");
        List<Chat> chats = jdbcChatRepository.gets();
        assertEquals(chats.size(), 2);
    }
}
