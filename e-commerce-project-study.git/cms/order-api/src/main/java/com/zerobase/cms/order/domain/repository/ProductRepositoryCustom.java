package com.zerobase.cms.order.domain.repository;


import com.zerobase.cms.order.domain.model.ProductEntity;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductEntity> searchByName(String name);
}
