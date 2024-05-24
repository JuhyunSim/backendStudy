package com.zerobase.cms.user.service;

import com.zerobase.cms.user.client.mailgun.MailGunClient;
import com.zerobase.cms.user.client.mailgun.SendingMailForm;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
class EmailSendServiceTest {

    @MockBean
    private MailGunClient mailGunClient;

    @Autowired
    private EmailSendService emailSendService;

    @Test
    void sendEmailTest() {


        //given
        String expectedMessage = "This is a test message";
        when(mailGunClient.sendEmail(any(SendingMailForm.class)))
                .thenReturn(ResponseEntity.ok(expectedMessage));

        //when
        String actualMessage = emailSendService.sendEmail();

        //then
        assertEquals(expectedMessage, actualMessage);

        // mailgunclient의 반환값 확인
        ArgumentCaptor<SendingMailForm> sendingMailFormCaptor = ArgumentCaptor.forClass(SendingMailForm.class);
        verify(mailGunClient).sendEmail(sendingMailFormCaptor.capture());
        SendingMailForm capturedForm = sendingMailFormCaptor.getValue();

        assertEquals("zerobase@myhome.com", capturedForm.getFrom());
        assertEquals("floweronwall31@gmail.com", capturedForm.getTo());
        assertEquals("This is test from my home", capturedForm.getSubject());
        assertEquals("안녕하세요. 제로베이스 테스트 메일 입니다.", capturedForm.getText());

    }

}