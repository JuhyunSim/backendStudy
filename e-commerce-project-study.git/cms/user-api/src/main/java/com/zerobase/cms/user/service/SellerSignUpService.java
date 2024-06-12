package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignupForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.domain.repository.SellerRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerSignUpService {

    private final SellerRepository sellerRepository;

    public Seller signup(SignupForm signupForm) {
        return sellerRepository.save(Seller.from(signupForm));
    }

    public boolean isEmail(String email) {
        return sellerRepository
                .findByEmail(email.toLowerCase(Locale.ROOT)).isPresent();
    }

    @Transactional
    public void verifySellerEmail(String email, String code) {
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NOT_FOUND_USER)
                );
        if (seller.isVerify()) {
            throw new CustomException(ErrorCode.ALREADY_VERIFIED_USER);
        }

        if (!seller.getVerificationCode().equals(code)) {
            throw new CustomException(ErrorCode.WRONG_VERIFICATION);
        }

        if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_CODE);
        }
        seller.setVerify(true);
    }

    @Transactional
    public LocalDateTime changeSellerVerification(Long customerId, String verificationCode) {
        Optional<Seller> optionalSeller =
                sellerRepository.findById(customerId);

        if (optionalSeller.isPresent()) {
            Seller seller = optionalSeller.get();
            seller.setVerificationCode(verificationCode);
            seller.setVerify(false);
            seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return seller.getVerifyExpiredAt();
        } else {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

    }
}
