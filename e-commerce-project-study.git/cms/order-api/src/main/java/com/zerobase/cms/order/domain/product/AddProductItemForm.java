package com.zerobase.cms.order.domain.product;

import com.zerobase.cms.order.domain.model.ProductItemEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProductItemForm {
    private Long productId;
    private String name;
    private Integer price;
    private Integer count;

}
