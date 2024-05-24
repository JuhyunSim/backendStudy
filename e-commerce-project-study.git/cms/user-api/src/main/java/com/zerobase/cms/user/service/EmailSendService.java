package com.zerobase.cms.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.user.client.mailgun.MailGunClient;
import com.zerobase.cms.user.client.mailgun.SendingMailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {

    private final MailGunClient mailGunClient;

    public String sendEmail() {
        SendingMailForm sendingMailForm = SendingMailForm.builder()
                .from("zerobase@myhome.com")
                .to("floweronwall31@gmail.com")
                .subject("This is test from my home")
                .text("안녕하세요. 제로베이스 테스트 메일 입니다.")
                .build();

        return mailGunClient.sendEmail(sendingMailForm).getBody();
    }



}

