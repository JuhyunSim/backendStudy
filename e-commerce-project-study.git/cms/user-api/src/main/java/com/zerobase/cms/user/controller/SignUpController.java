package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.application.SignUpApplication;
import com.zerobase.cms.user.domain.SignupForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpApplication signUpApplication;

    @PostMapping("/customer")
    public ResponseEntity<String> customerSignUp(@RequestBody SignupForm signupForm) {
        return ResponseEntity.ok(signUpApplication.customerSignUp(signupForm));
    }

    @PutMapping("verify/customer")
    public ResponseEntity<String> verifyCustomer(@RequestParam String email, String code) {
        signUpApplication.customerVerify(email, code);
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }

    @PostMapping("/seller")
    public ResponseEntity<String> sellerSignUp(@RequestBody SignupForm signupForm) {
        return ResponseEntity.ok(signUpApplication.sellerSignUp(signupForm));
    }

    @PutMapping("verify/customer")
    public ResponseEntity<String> verifySeller(@RequestParam String email, String code) {
        signUpApplication.sellerVerify(email, code);
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }
}
