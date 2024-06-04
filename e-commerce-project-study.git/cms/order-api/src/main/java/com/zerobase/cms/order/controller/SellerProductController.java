package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.domain.dto.ProductDto;
import com.zerobase.cms.order.domain.dto.ProductItemDto;
import com.zerobase.cms.order.domain.model.ProductEntity;
import com.zerobase.cms.order.domain.model.ProductItemEntity;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.product.UpdateProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.service.ProductItemService;
import com.zerobase.cms.order.service.ProductService;
import com.zerobase.cms.user.config.filter.CustomerFilter;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("seller/product")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;
    private final JwtAuthenticationProvider provider;
    private final ProductItemService productItemService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestHeader(name = "X-Auth-Token") String token,
                                        @RequestBody AddProductForm addProductForm
    ) {
        ProductEntity productEntity =
                productService.addProduct(
                        provider.getUserVo(token).getId(), addProductForm);
        return ResponseEntity.ok(ProductDto.from(productEntity));
    }

    @PostMapping("/add/item")
    public ResponseEntity<?> addProductItem(
            @RequestHeader(name = "X-Auth-Token") String token,
            @RequestBody AddProductItemForm addProductItemForm
    ) {
        ProductItemEntity productItemEntity
                = productItemService.addProductItem(
                        provider.getUserVo(token).getId(), addProductItemForm
        );
        return ResponseEntity.ok(ProductItemDto.from(productItemEntity));
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestHeader(name = "X-Auth-Token") String token,
                                        @RequestBody UpdateProductForm updateProductForm
    ) {
        ProductEntity productEntity =
                productService.updateProduct(
                        provider.getUserVo(token).getId(), updateProductForm);
        return ResponseEntity.ok(ProductDto.from(productEntity));
    }

    @PutMapping("/update/item")
    public ResponseEntity<?> updateProductItem(
            @RequestHeader(name = "X-Auth-Token") String token,
            @RequestBody UpdateProductItemForm updateProductItemForm
    ) {
        ProductItemEntity productItemEntity
                = productItemService.updateProductItem(
                provider.getUserVo(token).getId(), updateProductItemForm
        );
        return ResponseEntity.ok(ProductItemDto.from(productItemEntity));
    }

}
