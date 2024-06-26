package com.zerobase.cms.order.domain.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductForm {
    private Long id;
    private String name;
    private String description;
    private List<UpdateProductItemForm> items;
}
