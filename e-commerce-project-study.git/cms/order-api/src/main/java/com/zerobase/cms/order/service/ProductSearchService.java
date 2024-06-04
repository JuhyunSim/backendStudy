package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.ProductEntity;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.domain.repository.ProductRepositoryCustomImpl;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductRepository productRepository;
    private final ProductRepositoryCustomImpl productRepositoryCustom;

    public List<ProductEntity> searchByName(String name) {
        return productRepositoryCustom.searchByName(name);
    }

    public ProductEntity searchProduct(Long productId) {
        return productRepository.findWithItemsById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT)
        );
    }
    public List<ProductEntity> getListByProductId(List<Long> productsId) {
        return productRepository.findAllById(productsId);
    }
}
