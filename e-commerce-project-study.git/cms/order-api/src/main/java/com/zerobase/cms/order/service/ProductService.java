package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.ProductEntity;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductEntity addProduct(Long sellerId,
                                    AddProductForm addProductForm) {
        return productRepository.save(ProductEntity.of(sellerId, addProductForm));
    }
}
