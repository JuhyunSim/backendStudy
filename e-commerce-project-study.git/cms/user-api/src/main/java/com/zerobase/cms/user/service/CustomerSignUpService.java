package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignupForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
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
public class CustomerSignUpService {

    private final CustomerRepository customerRepository;

    public Customer signup(SignupForm signupForm) {
        return customerRepository.save(Customer.from(signupForm));
    }

    public boolean isEmail(String email) {
        return customerRepository
                .findByEmail(email.toLowerCase(Locale.ROOT)).isPresent();
    }

    @Transactional
    public void verifyCustomerEmail(String email, String code) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NOT_FOUND_USER)
                );
        if (customer.isVerify()) {
            throw new CustomException(ErrorCode.ALREADY_VERIFIED_USER);
        }

        if (!customer.getVerificationCode().equals(code)) {
            throw new CustomException(ErrorCode.WRONG_VERIFICATION);
        }

        if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_CODE);
        }
        customer.setVerify(true);
    }

    @Transactional
    public LocalDateTime changeCustomerVerification(Long customerId, String verificationCode) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setVerificationCode(verificationCode);
            customer.setVerify(false);
            customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return customer.getVerifyExpiredAt();
        } else {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

    }
}
