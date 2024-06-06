package com.zerobase.cms.order.domain.repository;

import com.zerobase.cms.order.domain.model.ProductEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>
        , ProductRepositoryCustom{
    @EntityGraph(attributePaths = {"items"}, type =
            EntityGraph.EntityGraphType.LOAD)
    Optional<ProductEntity> findBySellerIdAndId(Long sellerId, Long productId);

    @EntityGraph(attributePaths = {"items"}, type =
            EntityGraph.EntityGraphType.LOAD)
    Optional<ProductEntity> findWithItemsById(Long productId);

    @EntityGraph(attributePaths = {"items"}, type =
            EntityGraph.EntityGraphType.LOAD)
    List<ProductEntity> findAllByIdIn(List<Long> ids);

}
