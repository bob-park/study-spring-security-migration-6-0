package org.bobpark.studyspringsecurity.domain.api.message.controller;

import java.util.Collections;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/messages")
public class MessageController {

    @GetMapping(path = "")
    public Map<String, Object> apiMessage() {
        return Collections.singletonMap("data", "ok");
    }
}
