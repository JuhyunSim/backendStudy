package com.zerobase.cms.user.application;


import com.zerobase.cms.user.domain.SignInForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.CustomerService;
import com.zerobase.cms.user.service.SellerService;
import com.zerobase.domain.common.UserType;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignInApplication {
    private final CustomerService customerService;
    private final SellerService sellerService;
    private final JwtAuthenticationProvider provider;

    public String customerLoginToken(SignInForm signInForm) {
        // 로그인 가능 여부
        Customer customer = customerService.findValidCustomer(
                signInForm.getEmail(), signInForm.getPassword()
        ).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

        // 토큰 발행

        // 토큰을 response
        return provider.createToke(
                customer.getEmail(), customer.getId(), UserType.CUSTOMER);
    }

    public String sellerLoginToken(SignInForm signInForm) {
        // 로그인 가능 여부
        Seller seller = sellerService.findValidSeller(
                signInForm.getEmail(), signInForm.getPassword()
        ).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

        // 토큰 발행
        // 토큰을 response
        return provider.createToke(
                seller.getEmail(), seller.getId(), UserType.SELLER);
    }
}
