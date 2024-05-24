package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignupForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final CustomerRepository customerRepository;

    public Customer signup(SignupForm signupForm) {
        return customerRepository.save(Customer.from(signupForm));
    }
}
