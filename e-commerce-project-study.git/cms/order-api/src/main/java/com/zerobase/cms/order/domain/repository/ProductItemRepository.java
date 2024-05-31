package com.zerobase.cms.order.domain.repository;

import com.zerobase.cms.order.domain.model.ProductItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItemEntity, Long> {
}
