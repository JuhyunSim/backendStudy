package com.zerobase.cms.user.client.mailguntest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TestConfig {

    private final ObjectMapper objectMapper;
}
