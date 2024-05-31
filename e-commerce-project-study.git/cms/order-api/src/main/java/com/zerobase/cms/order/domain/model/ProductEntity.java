package com.zerobase.cms.order.domain.model;

import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.user.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;

    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductItemEntity> items = new ArrayList<>();

    public static ProductEntity of(Long sellerId, AddProductForm addProductForm) {
        return ProductEntity.builder()
                .sellerId(sellerId)
                .name(addProductForm.getName())
                .description(addProductForm.getDescription())
                .items(addProductForm.getAddProductItems().stream()
                        .map(piForms -> ProductItemEntity.of(sellerId, piForms))
                        .collect(Collectors.toList()))
                .build();
    }
}
