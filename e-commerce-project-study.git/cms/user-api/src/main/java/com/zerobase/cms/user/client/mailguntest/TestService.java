package com.zerobase.cms.user.client.mailguntest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final ObjectMapper objectMapper;
    private final String DOMAIN = "sandbox2a6a130782fb4842abc349c251d485d5.mailgun.org";
    private final String API_KEY = "97c21d6131a664c4259b13ebba491e02-a2dd40a3-191866b8";

    public String sendSimpleMessage() throws UnirestException, JsonProcessingException {

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN + "/messages")
			.basicAuth("api", API_KEY)
                .queryString("from", "Excited User <zerobase@zerobase.com>")
                .queryString("to", "floweronwall31@gmail.com")
                .queryString("subject", "hello")
                .queryString("text", "testing")
                .asJson();

        return request.getBody().toString();
    }
}