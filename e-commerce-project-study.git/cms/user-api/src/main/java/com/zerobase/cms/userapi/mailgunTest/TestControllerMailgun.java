package com.zerobase.cms.userapi.mailgunTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestControllerMailgun {

    private final TestService testService;

    @GetMapping("/test")
    ResponseEntity<String> testSending() throws UnirestException, JsonProcessingException {
        return ResponseEntity.ok(testService.sendSimpleMessage());
    }
}
