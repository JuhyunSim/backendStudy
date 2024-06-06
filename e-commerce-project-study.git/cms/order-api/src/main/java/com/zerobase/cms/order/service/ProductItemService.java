package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.ProductEntity;
import com.zerobase.cms.order.domain.model.ProductItemEntity;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductItemRepository;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ProductItemEntity getProductItem(Long productId) {
        return productItemRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM)
        );
    }

    @Transactional
    public ProductItemEntity addProductItem(Long sellerId, AddProductItemForm addProductItemForm) {
        //Product 존재 여부 확인
        ProductEntity productEntity = productRepository.findBySellerIdAndId(sellerId, addProductItemForm.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        //추가하려는 아이템명의 중복여부 확인
        if (productEntity.getItems().stream().anyMatch(item -> item.getName().equals(addProductItemForm.getName()))) {
            throw new CustomException(ErrorCode.SAME_ITEM_NAME);
        }

        ProductItemEntity productItemEntity = ProductItemEntity.of(sellerId, addProductItemForm);
        productEntity.getItems().add(productItemEntity);
        return productItemEntity;
    }

    @Transactional
    public ProductItemEntity updateProductItem(Long sellerId, UpdateProductItemForm updateProductItemForm) {
        ProductItemEntity productItemEntity = productItemRepository.findById(updateProductItemForm.getId())
                .filter(pi -> pi.getSellerId().equals(sellerId))
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT)
                );

        productItemEntity.setName(updateProductItemForm.getName());
        productItemEntity.setCount(updateProductItemForm.getCount());
        productItemEntity.setPrice(updateProductItemForm.getPrice());
        return productItemEntity;
    }

    @Transactional
    public void deleteProductItem(Long sellerId, Long productId) {
        ProductItemEntity productItemEntity = productItemRepository.findById(productId).filter(
                pi -> pi.getSellerId().equals(sellerId)
        ).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT)
        );

        productItemRepository.delete(productItemEntity);
    }
}
