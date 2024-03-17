package org.java.scrapper.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.java.scrapper.dto.chat.ChatRequest;
import org.java.scrapper.dto.chat.ChatResponse;
import org.java.scrapper.jdbc.JdbcChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scrapper")
@RequiredArgsConstructor
public class ChatController {
    private final JdbcChatService jdbcChatService;

    @PostMapping("/chats")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid ChatRequest request) {
        jdbcChatService.save(request);
    }

    @DeleteMapping("/chats/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        jdbcChatService.delete(id);
    }

    @GetMapping("/chats/{id}")
    public ChatResponse get(@PathVariable Long id) {
        return jdbcChatService.get(id);
    }

    @GetMapping("/chats")
    public List<ChatResponse> gets() {
        return jdbcChatService.gets();
    }
}
