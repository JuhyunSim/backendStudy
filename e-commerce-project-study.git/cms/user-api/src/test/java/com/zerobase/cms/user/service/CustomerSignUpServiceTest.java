package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignupForm;
import com.zerobase.cms.user.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerSignUpServiceTest {

    @Autowired
    private CustomerSignUpService customerSignUpService;

    @Test
    void signUpTest() {
        //given
        SignupForm signupForm = SignupForm.builder()
                .name("Kim")
                .email("kim@zerobase.com")
                .password("123456")
                .birth(LocalDate.of(2024, 1, 1))
                .build();

        //when
        Customer c = customerSignUpService.signup(signupForm);

        //then
        assertEquals("Kim", signupForm.getName());
    }
}