package com.zerobase.cms.user.client.mailgun;

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
