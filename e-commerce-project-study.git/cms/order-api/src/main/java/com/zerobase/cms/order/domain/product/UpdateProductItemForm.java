package com.zerobase.cms.order.domain.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductItemForm {
    private Long id;
    private Long productId;
    private String name;
    private Integer price;
    private Integer count;
}
