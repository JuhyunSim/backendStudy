package com.zerobase.cms.order.domain.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductCartForm {
    private Long productId;
    private Long sellerId;
    private String name;
    private String description;
    private List<ProductItem> items;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductItem{
        private Long id;
        private String name;
        private Integer count;
        private Integer price;
    }
}
