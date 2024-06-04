package com.zerobase.cms.order.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.cms.order.domain.model.ProductEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private Long sellerId;

    @JsonProperty("name")
    private String name;
    private String description;
    private List<ProductItemDto> items;

    public static ProductDto from(ProductEntity productEntity) {
        return ProductDto.builder()
                .id(productEntity.getId())
                .sellerId(productEntity.getSellerId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .items(productEntity.getItems().stream()
                        .map(ProductItemDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
