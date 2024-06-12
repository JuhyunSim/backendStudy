package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public Optional<Seller> findValidSeller(String email, String password) {
        return sellerRepository.findByEmail(email).stream()
                .filter(seller -> seller.getPassword().equals(password))
                .findFirst();
    }
}
