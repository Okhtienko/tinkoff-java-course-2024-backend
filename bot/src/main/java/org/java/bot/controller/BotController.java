package org.java.bot.controller;

import lombok.RequiredArgsConstructor;
import org.java.bot.dto.LinkUpdateRequest;
import org.java.bot.service.UpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bot")
@RequiredArgsConstructor
public class BotController {
    private final UpdateService updateService;

    @PostMapping("/updates")
    public ResponseEntity<String> sendUpdate(@RequestBody LinkUpdateRequest request) {
        updateService.sendUpdate(request);
        return ResponseEntity.ok().body("Send update");
    }
}
