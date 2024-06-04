package com.zerobase.cms.order.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.cms.order.domain.model.ProductEntity;
import com.zerobase.cms.order.domain.model.QProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductEntity> searchByName(String name) {

        String searchName = "%" + name + "%";
        QProductEntity product =  QProductEntity.productEntity;
        return queryFactory.selectFrom(product)
                .where(product.name.like(searchName)).fetch();
    }
}
