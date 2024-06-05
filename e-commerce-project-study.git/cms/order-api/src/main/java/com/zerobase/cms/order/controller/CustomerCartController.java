package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.application.CartApplication;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.service.CartService;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
public class CustomerCartController {

    private final CartApplication cartApplication;
    private final JwtAuthenticationProvider provider;

    @PostMapping("/add")
    public ResponseEntity<Cart> addCart(@RequestHeader("X-Auth-Token") String token,
            @RequestBody AddProductCartForm addProductCartForm) {

        return ResponseEntity.ok(cartApplication.addCart(
                provider.getUserVo(token).getId(), addProductCartForm)
        );

    }
}
