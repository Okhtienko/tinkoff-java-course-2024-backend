package org.java.scrapper.controller;

import jakarta.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.java.scrapper.dto.LinkRequest;
import org.java.scrapper.dto.LinkResponse;
import org.java.scrapper.service.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scrapper")
@RequiredArgsConstructor
public class ScrapperController {
    private final LinkService linkService;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@PathVariable Long id) {
        linkService.register(id);
        return "Registration successful";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        linkService.delete(id);
    }

    @GetMapping("/links")
    public List<String> gets(@RequestHeader("Tg-Chat-Id") Long chatId) {
        return linkService.gets(chatId);
    }

    @PostMapping("/links")
    public LinkResponse save(
        @RequestHeader("Tg-Chat-Id") Long id,
        @RequestBody @Valid LinkRequest request) throws URISyntaxException {
        return linkService.save(id, request.getUrl());
    }

    @DeleteMapping("/links")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(
        @RequestHeader("Tg-Chat-Id") Long id,
        @RequestBody  @Valid LinkRequest request) throws URISyntaxException {
        linkService.remove(id, request.getUrl());
    }
}
