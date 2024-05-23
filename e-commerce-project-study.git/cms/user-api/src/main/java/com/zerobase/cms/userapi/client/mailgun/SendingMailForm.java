package com.zerobase.cms.userapi.client.mailgun;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendingMailForm {
    private String from;
    private String to;
    private String subject;
    private String text;
}
