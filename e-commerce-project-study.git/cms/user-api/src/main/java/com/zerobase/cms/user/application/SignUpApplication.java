package com.zerobase.cms.user.application;

import com.zerobase.cms.user.client.mailgun.MailGunClient;
import com.zerobase.cms.user.client.mailgun.SendingMailForm;
import com.zerobase.cms.user.domain.SignupForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {

    private final MailGunClient mailGunClient;
    private final SignUpService signUpService;

    public void customerVerify(String email, String code) {
        signUpService.verifyEmail(email, code);
    }

    public String customerSignUp(SignupForm form) {

        if (signUpService.isEmail(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
        } else {
            Customer customer = signUpService.signup(form);
            String code = getRandomCode();

            SendingMailForm sendingMailForm = SendingMailForm.builder()
                    .from("zerobase@zerobase.com")
                    .to(customer.getEmail())
                    .subject("Verify your email")
                    .text(getValidateEmailBody(customer.getEmail(),
                            customer.getName(), code))
                    .build();
            mailGunClient.sendEmail(sendingMailForm);
            signUpService.changeCustomerVerification(customer.getId(), code);
        }
        return "회원가입에 성공하였습니다.";
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getValidateEmailBody(String email, String name, String code) {
        StringBuilder sb = new StringBuilder();
        return sb.append("Hello ")
                .append(name)
                .append("! Please click link for verification\n\n")
                .append("http://localhost:8080/signup/verify/customer?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }

}
