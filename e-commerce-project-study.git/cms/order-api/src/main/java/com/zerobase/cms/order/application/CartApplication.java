package com.zerobase.cms.order.application;

import com.zerobase.cms.order.domain.model.ProductEntity;
import com.zerobase.cms.order.domain.model.ProductItemEntity;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.service.CartService;
import com.zerobase.cms.order.service.ProductSearchService;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartApplication {

    private final ProductSearchService productSearchService;
    private final CartService cartService;

    public Cart addCart(Long customerId, AddProductCartForm addProductCartForm) {
        ProductEntity productEntity =
                productSearchService
                        .searchProduct(addProductCartForm.getProductId());

        Cart cart = cartService.getCart(customerId);
        //다른 상품을 추가할 때
        boolean notInCartProduct = cart.getProducts()
                .stream()
                .noneMatch(pd -> addProductCartForm.getProductId().equals(pd.getId()));

        if (cart != null) {
            if (notInCartProduct) {
                if (!notInCartProductAddable(productEntity, addProductCartForm)) {
                    throw new CustomException(ErrorCode.ITEM_COUNT_NOT_ENOUGH);
                } else {
                    return cartService.addCart(customerId, addProductCartForm);
                }
            } else {
                //카트에 있는 같은 상품의 아이템을 장바구니에 추가할 때
                if (cart != null && !alreadyInCartAddAble(cart, productEntity, addProductCartForm)) {
                    throw new CustomException(ErrorCode.ITEM_COUNT_NOT_ENOUGH);
                } else {
                    return cartService.addCart(customerId, addProductCartForm);
                }
            }
        }
        return cartService.addCart(customerId, addProductCartForm);
    }

    private boolean alreadyInCartAddAble(Cart cart, ProductEntity productEntity, AddProductCartForm addProductCartForm) {
        //카트에 있는 상품에 아이템을 추가할 때
        Cart.Product cartProduct = cart.getProducts()
                .stream()
                .filter(product -> product.getId().equals(addProductCartForm.getProductId()))
                .findFirst()
                .orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT)
        );

        Map<Long, Integer> cartItemCount = cartProduct.getItems()
                .stream()
                .collect(Collectors.toMap(Cart.ProductItem::getId,
                        Cart.ProductItem::getCount)
                );

        Map<Long, Integer> dbItemCount = productEntity.getItems()
                .stream()
                        .collect(Collectors.toMap(
                                ProductItemEntity::getId,
                                ProductItemEntity::getCount
                        ));

        return addProductCartForm.getItems()
                .stream()
                .noneMatch(productItem -> {
                            Integer cartCount = cartItemCount.get(productItem.getId());
                            Integer dbCount = dbItemCount.get(productItem.getId());
                            return cartCount + productItem.getCount() > dbCount;
                        }
                );
    }

    private boolean notInCartProductAddable(ProductEntity productEntity,
                                            AddProductCartForm addProductCartForm) {

        Map<Long, Integer> dbItemCount = productEntity.getItems()
                .stream()
                .collect(Collectors.toMap(
                        ProductItemEntity::getId,
                        ProductItemEntity::getCount
                ));

        return addProductCartForm.getItems()
                .stream()
                .noneMatch(formItem
                        -> dbItemCount.get(formItem.getId()) - formItem.getCount() < 0
                );
    }
}
