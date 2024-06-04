package com.zerobase.cms.order.domain.model;

import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.user.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity(name = "product_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class ProductItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;

    @Audited
    private String name;
    @Audited
    private Integer price;

    private Integer count;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    public static ProductItemEntity of(Long sellerId, AddProductItemForm addProductItemForm) {
        return ProductItemEntity.builder()
                .sellerId(sellerId)
                .name(addProductItemForm.getName())
                .price(addProductItemForm.getPrice())
                .count(addProductItemForm.getCount())
                .build();
    }
}
