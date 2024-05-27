package com.zerobase.cms.user.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInForm {
    private String email;
    private String password;
}
