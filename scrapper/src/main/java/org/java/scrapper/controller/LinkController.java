package org.java.scrapper.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.java.scrapper.domain.LinkManagementService;
import org.java.scrapper.dto.link.LinkRequest;
import org.java.scrapper.dto.link.LinkResponse;
import org.java.scrapper.service.LinkService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scrapper")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;
    private final LinkManagementService linkServiceImp;

    @PostMapping("/links")
    public LinkResponse save(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody @Valid LinkRequest request) {
        return linkServiceImp.save(request, id);
    }

    @GetMapping("/links")
    public List<LinkResponse> gets(@RequestHeader("Tg-Chat-Id") Long id) {
        return linkService.gets(id);
    }

    @GetMapping("/link")
    public LinkResponse get(@RequestHeader("Tg-Chat-Id") Long id, @Valid LinkRequest request) {
        return linkService.get(request, id);
    }

    @DeleteMapping("/link")
    public LinkResponse remove(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody @Valid LinkRequest request) {
       return linkService.remove(request, id);
    }
}
