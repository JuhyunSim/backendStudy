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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

        if (cart != null && !addAble(cart, productEntity, addProductCartForm)) {
            throw new CustomException(ErrorCode.ITEM_COUNT_NOT_ENOUGH);
        }

        return cartService.addCart(customerId, addProductCartForm);
    }

    private boolean addAble(Cart cart, ProductEntity productEntity, AddProductCartForm addProductCartForm) {
        Cart.Product cartProduct = cart.getProducts()
                .stream()
                .filter(product -> product.getId().equals(addProductCartForm.getProductId()))
                .findFirst()
                .orElse(Cart.Product.builder()
                        .id(productEntity.getId())
                        .items(Collections.emptyList())
                        .build());


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
                            if (cartCount == null) {
                                cartCount = 0;
                            }
                            Integer dbCount = dbItemCount.get(productItem.getId());
                            return cartCount + productItem.getCount() > dbCount;
                        }
                );
    }

    public Cart updateCart(Long customerId, Cart cart) {
        cartService.putCart(customerId, cart);
        return getCart(customerId);
    }

    //1. 장바구니에 상품 추가
    //2. 상품의 가격이나 수량이 변동될 수 있음

    public Cart getCart(Long customerId) {
        Cart cart = refreshCart(cartService.getCart(customerId));
        Cart returnCart = new Cart();
        returnCart.setProducts(cart.getProducts());
        returnCart.setCustomerId(cart.getCustomerId());
        returnCart.setMessages(cart.getMessages());
        cart.setMessages(new ArrayList<>());
        //메세지 제거한 카트를 저장함
        cartService.putCart(customerId, cart);
        return returnCart;
    }


    protected Cart refreshCart(Cart cart) {
        //1. 상품 or 상품 아이템의 변동사항(가격, 수량 등) 체크 -> 변동사항에 맞게 알람 전송
        //2. 임의로 장바구니 내의 수량, 가격을 변동시킴 (변동 여부에 대해서도 알람 제공)
        for (int i = 0; i < cart.getProducts().size(); i++) {
            Cart.Product cartProduct = cart.getProducts().get(i);
            ProductEntity productEntity =
                    productSearchService.searchProduct(cartProduct.getId());
            //장바구니에 담은 상품이 데이터베이스에서 삭제되었을
            if (productEntity == null) {
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMessage(cartProduct.getName() +
                        "이 삭제되었습니다.");
                continue;
            }

            Map<Long, ProductItemEntity> productItemMap
                     = productEntity.getItems().stream()
                    .collect(Collectors.toMap(
                            ProductItemEntity::getId, pi -> pi)
                    );

            List<String> tmpMessage = new ArrayList<>();
            for(int j = 0; j < cartProduct.getItems().size(); j++) {
                Cart.ProductItem cartProductItem = cartProduct.getItems().get(j);
                ProductItemEntity productItemEntity =
                        productItemMap.get(cartProductItem.getId());
                if (productItemEntity == null) {
                    cartProduct.getItems().remove(cartProductItem);
                    j--;
                    tmpMessage.add(cartProductItem.getName() + " 옵션이 삭제되었습니다.");
                    continue;
                }

                boolean isPriceChanged = false;
                boolean isCountChanged = false;
                if (!cartProductItem.getPrice().equals(
                        productItemMap.get(cartProductItem.getId()).getPrice())
                ) {
                    isPriceChanged = true;
                    cartProductItem.setPrice(productItemEntity.getPrice());
                }

                if (cartProductItem.getCount() >
                        productItemMap.get(cartProductItem.getId()).getCount()
                ) {
                    isCountChanged = true;
                    cartProductItem.setCount(productItemEntity.getCount());
                }
                
                if (isPriceChanged && isCountChanged) {
                    tmpMessage.add(cartProductItem.getName()
                            + " 가격이 변동되었고 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
                } else if (isPriceChanged) {
                    tmpMessage.add(cartProductItem.getName()
                            + " 가격이 변동되었었습니다.");
                } else if (isCountChanged) {
                    tmpMessage.add(cartProductItem.getName()
                            + " 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
                }
            }

            //모든 옵션이 삭제되었을 때
            if (cartProduct.getItems().size() == 0) {
                cart.getProducts().remove(cartProduct);
                cart.addMessage(cartProduct.getName() +
                        " 상품의 옵션이 모두 없어져 구매가 불가능합니다.");
            } else if (tmpMessage.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(cartProduct.getName() + " 상품 변동사항: ");
                for (String s : tmpMessage) {
                    stringBuilder.append(s);
                    stringBuilder.append(", ");
                }
                cart.addMessage(stringBuilder.toString());
            }
        }
        return cartService.putCart(cart.getCustomerId(), cart);
    }

    public void clearCart(Long customerId) {
        cartService.putCart(customerId, null);
    }
}
