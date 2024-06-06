package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.customer.CustomerDto;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Optional<Customer> findByIdAndEmail(Long id, String email) {
        return customerRepository.findById(id).stream()
                .filter(customer -> customer.getEmail().equals(email))
                .findFirst();
    }

    public Optional<Customer> findValidCustomer(String email, String password) {
        return customerRepository.findByEmail(email).stream()
                .filter(customer -> customer.getPassword().equals(password))
                .findFirst();
    }

    public Integer changeBalance(Long customerId,
                                     ChangeBalanceForm changeBalanceForm) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );
        if (customer.getBalance() == null) {
            customer.setBalance(0);
        }
        customer.setBalance(
                customer.getBalance() + changeBalanceForm.getChangeAmount()
        );
        Customer changedCustomer = customerRepository.save(customer);
        return CustomerDto.from(changedCustomer).getBalance();
    }
}
