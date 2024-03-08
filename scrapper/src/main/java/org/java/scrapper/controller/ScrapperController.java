package org.java.scrapper.controller;

import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.java.scrapper.dto.LinkRequest;
import org.java.scrapper.dto.LinkResponse;
import org.java.scrapper.service.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scrapper")
@RequiredArgsConstructor
public class ScrapperController {
    private final LinkService linkService;

    @PostMapping("/{id}")
    public ResponseEntity<String> register(@PathVariable Long id) {
        linkService.register(id);
        return ResponseEntity.ok().body("Registration successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        linkService.delete(id);
        return ResponseEntity.ok().body("Deletion successful");
    }

    @GetMapping("/links")
    public List<LinkResponse> gets(@RequestHeader("Tg-Chat-Id") Long chatId) {
        return linkService.gets(chatId);
    }

    @PostMapping("/links")
    public LinkResponse save(
        @RequestHeader("Tg-Chat-Id") Long id,
        @RequestBody LinkRequest request) throws URISyntaxException {
        return linkService.save(id, request.getUrl());
    }

    @DeleteMapping("/links")
    public LinkResponse remove(
        @RequestHeader("Tg-Chat-Id") Long id,
        @RequestBody LinkRequest request) throws URISyntaxException {
        return linkService.remove(id, request.getUrl());
    }
}
