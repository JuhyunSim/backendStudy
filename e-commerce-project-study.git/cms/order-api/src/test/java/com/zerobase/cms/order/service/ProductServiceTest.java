package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.ProductEntity;
import com.zerobase.cms.order.domain.model.ProductItemEntity;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void saveProduct() {
        //given
        Long sellerId = 1L;
        int count = 3;
        AddProductForm addProductForm = AddProductForm.builder()
                .name("나이키 에어포스")
                .description("흰색")
                .addProductItems(new ArrayList<AddProductItemForm>())
                .build();
        for (int i = 0; i < count; i++) {
            addProductForm.getAddProductItems().add(AddProductItemForm.builder()
                            .productId(sellerId)
                            .price(1000)
                            .count(count)
                            .name(addProductForm.getName() + "(" + i + ")")
                            .build());
        }

        ProductEntity productEntity = ProductEntity.of(sellerId, addProductForm);
        given(productRepository.save(any(ProductEntity.class)))
                .willReturn(productEntity);

        //when
        ProductEntity result = productService.addProduct(sellerId, addProductForm);

        //then
        assertNotNull(result);
        assertEquals(1L, result.getSellerId());
        assertEquals(addProductForm.getName(), result.getName());
        assertEquals(addProductForm.getDescription(), result.getDescription());
        assertEquals(3, result.getItems().size());
        assertEquals("나이키 에어포스(0)", result.getItems().get(0).getName());
        assertEquals("나이키 에어포스(1)", result.getItems().get(1).getName());
        assertEquals("나이키 에어포스(2)", result.getItems().get(2).getName());
    }
}