package com.zerobase.cms.order.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.cms.order.domain.model.ProductItemEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDto {
    private Long id;
    private Long sellerId;

    @JsonProperty("name")
    private String name;
    private Integer price;
    private Integer count;

    public static ProductItemDto from(ProductItemEntity productItemEntity) {
        return ProductItemDto.builder()
                .id(productItemEntity.getId())
                .sellerId(productItemEntity.getSellerId())
                .count(productItemEntity.getCount())
                .price(productItemEntity.getPrice())
                .build();
    }

}
