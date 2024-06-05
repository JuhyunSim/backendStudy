package com.zerobase.cms.order.application;


import com.zerobase.cms.order.domain.model.ProductEntity;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest//(classes = TestRedisConfig.class)
class CartApplicationTest {

    @Autowired
    private CartApplication cartApplication;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void addAndRefreshTest() {

        ProductEntity productEntity = addProduct();
        ProductEntity result =
                productRepository.findWithItemsById(productEntity.getId()).get();

        assertNotNull(result);
        /////////

        productEntity.getItems().get(0).setCount(0);
    }

    private ProductEntity addProduct() {
        Long sellerId = 1L;

        AddProductForm addProductForm = AddProductForm.builder()
                .name("나이키 에어포스")
                .description("흰색")
                .addProductItems(new ArrayList<AddProductItemForm>())
                .build();

        return productService.addProduct(sellerId, addProductForm);
    }



}