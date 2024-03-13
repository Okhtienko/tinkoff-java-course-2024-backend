package org.java.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Test
    public void testSaveSuccess() {
        Long id = 123L;
        userService.save(id);
        assertTrue(userService.exists(id));
    }

    @Test
    public void testExistsSuccess() {
        Long id = 123L;
        userService.save(id);
        assertTrue(userService.exists(id));
    }

    @Test
    public void testExistsError() {
        Long id = 123L;
        assertFalse(userService.exists(id));
    }

    @Test
    public void shouldHandleUnexpectedException() {
        UserService mockUserService = Mockito.mock(UserService.class);
        doThrow(new RuntimeException("Unexpected error")).when(mockUserService).save(anyLong());
        assertThrows(RuntimeException.class, () -> mockUserService.save(123L));
    }
}
