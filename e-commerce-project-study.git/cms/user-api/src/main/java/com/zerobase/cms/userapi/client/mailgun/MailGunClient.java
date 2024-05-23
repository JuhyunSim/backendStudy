package com.zerobase.cms.userapi.client.mailgun;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3")
@Qualifier("mailgun")
public interface MailGunClient {

    @PostMapping("/sandbox2a6a130782fb4842abc349c251d485d5.mailgun.org/messages")
    ResponseEntity<String> sendEmail(@SpringQueryMap SendingMailForm sendingMailForm);


}
