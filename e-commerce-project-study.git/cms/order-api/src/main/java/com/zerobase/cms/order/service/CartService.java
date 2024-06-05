package com.zerobase.cms.order.service;

import com.zerobase.cms.order.client.RedisClient;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final RedisClient redisClient;

    public Cart getCart(Long customerId) {
        Cart cart = redisClient.get(customerId, Cart.class);
        return cart == null ? new Cart(customerId) : cart;
    }


    public Cart putCart(Long customerId, Cart cart) {
        redisClient.put(customerId, cart);
        return cart;
    }

    public Cart addCart(Long customerId, AddProductCartForm addProductCartForm) {

        Cart cart = redisClient.get(customerId, Cart.class);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
        }
        //이전에 같은 상품이 있는지?
        Optional<Cart.Product> productOptional = cart.getProducts()
                .stream()
                .filter(
                        product -> product.getId()
                                .equals(addProductCartForm.getProductId()))
                .findFirst();

        if (productOptional.isPresent()) {
            Cart.Product redisProduct = productOptional.get();
            List<Cart.ProductItem> productItems =
                    addProductCartForm.getItems()
                            .stream()
                            .map(Cart.ProductItem::from)
                            .collect(Collectors.toList());

            Map<Long, Cart.ProductItem> redisItemMap =
                    redisProduct.getItems().stream()
                            .collect(Collectors.toMap(
                                    Cart.ProductItem::getId, item -> item)
                            );
            //장바구니에 넣으려고 하는 상품의 이름이 변경되었을 때
            if (!redisProduct.getName().equals(addProductCartForm.getName())) {
                cart.addMessage(redisProduct.getName()
                        + "의 정보가 변경되었습니다. 확인 부탁드립니다.");
//                redisProduct.setName(addProductCartForm.getName());
            }

            for (Cart.ProductItem item : productItems) {
                Cart.ProductItem redisItem = redisItemMap.get(item.getId());
                if (redisItem == null) {
                    //기존에 없던 새로운 아이템을 추가하려고 할 때
                    redisProduct.getItems().add(item);
                } else {
                    if (!redisItem.getPrice().equals(item.getPrice())) {
                        cart.addMessage(redisProduct.getName()
                                + redisItem.getName() + "의 가격이 변경되었습니다.");
                    }
                    redisItem.setCount(item.getCount() + redisItem.getCount());
                }
            }
            redisClient.put(customerId, cart);
            return cart;

        } else {
            Cart.Product product = Cart.Product.from(addProductCartForm);
            cart.getProducts().add(product);
            redisClient.put(customerId, cart);
            return cart;
        }
    }

}
