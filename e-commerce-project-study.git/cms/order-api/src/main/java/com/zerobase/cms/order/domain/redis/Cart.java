package com.zerobase.cms.order.domain.redis;

import com.zerobase.cms.order.domain.product.AddProductCartForm;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("cart")
public class Cart {
    @Id
    private Long customerId;
    private List<Product> products = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    public Cart (Long customerId) {
        this.customerId = customerId;
    }

    public void addMessage(String message) {
        messages.add(message);
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Product {
        private Long id;
        private Long sellerId;
        private String name;
        private String description;
        private List<ProductItem> items = new ArrayList<>();

        public static Product from(AddProductCartForm addProductCartForm) {
            return Product.builder()
                    .id(addProductCartForm.getProductId())
                    .sellerId(addProductCartForm.getSellerId())
                    .name(addProductCartForm.getName())
                    .description(addProductCartForm.getDescription())
                    .items(addProductCartForm.getItems().stream()
                            .map(Cart.ProductItem::from)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductItem {
        private Long id;
        private String name;
        private Integer count;
        private Integer price;

        public static ProductItem from(
                AddProductCartForm.ProductItem addProductItemCartForm
        ) {
            return ProductItem.builder()
                    .id(addProductItemCartForm.getId())
                    .name(addProductItemCartForm.getName())
                    .count(addProductItemCartForm.getCount())
                    .price(addProductItemCartForm.getPrice())
                    .build();
        }
    }

    public Cart clone() {
        return new Cart(customerId, products, messages);
    }
}
