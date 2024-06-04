package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.ProductEntity;
import com.zerobase.cms.order.domain.model.ProductItemEntity;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
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

    @Transactional
    public ProductEntity updateProduct(Long sellerId,
                                       UpdateProductForm updateProductForm) {
        ProductEntity productEntity = productRepository.findBySellerIdAndId(sellerId, updateProductForm.getId())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT)
                );

        productEntity.setName(updateProductForm.getName());
        productEntity.setDescription(updateProductForm.getDescription());

        for(UpdateProductItemForm itemForm : updateProductForm.getItems()) {
            ProductItemEntity item = productEntity.getItems().stream()
                    .filter(pi -> pi.getId().equals(itemForm.getId()))
                    .findFirst().orElseThrow(
                            () -> new CustomException(ErrorCode.NOT_FOUND_ITEM)
                    );
            item.setName(itemForm.getName());
            item.setCount(itemForm.getCount());
            item.setPrice(itemForm.getPrice());
        }
        return productEntity;
    }

    @Transactional
    public void deleteProductEntity(Long sellerId, Long productId) {
        ProductEntity productEntity = productRepository.findBySellerIdAndId(sellerId, productId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT)
        );

        productRepository.delete(productEntity);
    }
}
