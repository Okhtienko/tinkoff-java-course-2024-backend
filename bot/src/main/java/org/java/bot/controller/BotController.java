package org.java.bot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.java.bot.dto.LinkUpdateRequest;
import org.java.bot.service.UpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bot")
@RequiredArgsConstructor
public class BotController {
    private final UpdateService updateService;

    @PostMapping("/updates")
    @ResponseStatus(HttpStatus.CREATED)
    public String sendUpdate(@RequestBody @Valid LinkUpdateRequest request) {
        updateService.sendUpdate(request);
        return "Update processed successfully";
    }
}
