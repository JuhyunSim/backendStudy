package com.zerobase.cms.order.domain.product;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductForm {
    private String name;
    private String description;
    private List<AddProductItemForm> addProductItems;


}
