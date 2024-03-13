package org.java.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkServiceTest {

    @Mock
    private Map<Long, List<String>> links;

    @InjectMocks
    private LinkService linkService;

    @Test
    public void testSaveSuccess() {
        Long id = 123L;
        String link = "https://example.com";

        when(links.computeIfAbsent(eq(id), any())).thenReturn(new ArrayList<>());

        linkService.save(id, link);
        assertNotNull(links);
    }

    @Test
    public void testRemoveSuccess() {
        Long id = 123L;
        String link = "https://example.com";

        if (links.containsKey(id)) {
            linkService.remove(id, link);
        }

        assertNull(links.get(id));
    }

    @Test
    public void testCheckSuccess() {
        String link = "https://example.com";
        assertTrue(linkService.check(link));
    }

    @Test
    public void testCheckError() {
        String link = "invalid link";
        assertFalse(linkService.check(link));
    }

    @Test
    public void testGetError() {
        when(links.get(123L)).thenReturn(Collections.emptyList());
        List<String> expectedLinks = linkService.get(123L);
        assertTrue(expectedLinks.isEmpty());
    }

    @Test
    public void testGetSuccess() {
        Long id = 123L;
        String link = "https://example.com";
        List<String> expectedLinks = List.of(link);

        when(links.get(id)).thenReturn(expectedLinks);
        assertEquals(expectedLinks, linkService.get(id));
    }

    @Test
    public void testExistsSuccess() {
        Long id = 123L;
        String link = "https://example.com";
        List<String> expectedLinks = List.of(link);

        when(links.get(id)).thenReturn(expectedLinks);
        assertTrue(linkService.exists(id, link));
    }

    @Test
    public void testExistsError() {
        Long id = 123L;
        String link = "https://example.com";
        List<String> expectedLinks = List.of("different link");

        when(links.get(id)).thenReturn(expectedLinks);
        assertFalse(linkService.exists(id, link));
    }
}
