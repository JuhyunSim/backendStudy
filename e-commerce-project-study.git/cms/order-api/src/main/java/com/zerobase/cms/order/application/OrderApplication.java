package com.zerobase.cms.order.application;

import com.zerobase.cms.order.client.UserClient;
import com.zerobase.cms.order.domain.model.ProductItemEntity;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.service.ProductItemService;
import com.zerobase.cms.order.service.ProductService;
import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.customer.CustomerDto;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class OrderApplication {
    //1. 물건이 주문 가능 상태인지 확인
    //2. 가격변동 있었는지 확인
    //3. 잔액 충분한지 확인
    //4. 결제 & 상품 재고 관리

    private final CartApplication cartApplication;
    private final UserClient userClient;
    private final ProductItemService productItemService;
    private final ProductService productService;

    @Transactional
    public void order(String token, Cart cart) {
        Integer moneyToPay = getTotalPrice(cart);
        CustomerDto customerDto = userClient.getinfo(token).getBody();
        Cart orderCart = cartApplication.refreshCart(cart);

        ChangeBalanceForm changeBalanceForm =
                ChangeBalanceForm.builder()
                        .from("USER")
                        .changeAmount(-moneyToPay)
                        .message("ORDER")
                        .build();

        if (orderCart.getMessages().size() > 0) {
            throw new CustomException(ErrorCode.ORDER_FAIL_CHECK_CART);
        }

        if (moneyToPay > customerDto.getBalance()) {
            throw new CustomException(ErrorCode.ORDER_FAIL_NOT_ENOUGH_BALANCE);
        }

        //결재하려는 찰나에 user 쪽에서 잔액이 변경될 경우에는 rollback (전략 보완 필요)
        Integer customerBalance =
                userClient.changeBalance(token, changeBalanceForm).getBody();

        //재고관리
        for(Cart.Product product : orderCart.getProducts()) {
            for(Cart.ProductItem cartProductItem : product.getItems()) {
                ProductItemEntity productItem =
                        productItemService.getProductItem(cartProductItem.getId());
                productItem.setCount(
                        productItem.getCount() - cartProductItem.getCount()
                );
            }
        }
    }


    @Transactional
    public void selectOrder(String token, Cart cart, List<Long> productIds) {
        Integer moneyToPay = getSelectedPrice(cart, productIds);
        CustomerDto customerDto = userClient.getinfo(token).getBody();
        Cart orderCart = cartApplication.refreshCart(cart);

        ChangeBalanceForm changeBalanceForm =
                ChangeBalanceForm.builder()
                        .from("USER")
                        .changeAmount(-moneyToPay)
                        .message("ORDER")
                        .build();

        if (orderCart.getMessages().size() > 0) {
            throw new CustomException(ErrorCode.ORDER_FAIL_CHECK_CART);
        }

        if (moneyToPay > customerDto.getBalance()) {
            throw new CustomException(ErrorCode.ORDER_FAIL_NOT_ENOUGH_BALANCE);
        }

        //결재하려는 찰나에 user 쪽에서 잔액이 변경될 경우에는 rollback (전략 보완 필요)
        Integer customerBalance =
                userClient.changeBalance(token, changeBalanceForm).getBody();

        //재고관리
        for(Long id : productIds) {
            Cart.Product product = orderCart.getProducts().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst().orElseThrow(
                            () -> new CustomException(ErrorCode.ORDER_FAIL_CHECK_CART)
                    );
            for(Cart.ProductItem cartProductItem : product.getItems()) {
                ProductItemEntity productItem =
                        productItemService.getProductItem(cartProductItem.getId());
                productItem.setCount(productItem.getCount() - cartProductItem.getCount());
            }
        }
    }

    private Integer getTotalPrice(Cart cart) {
        return cart.getProducts().stream().flatMapToInt(
                product -> product.getItems().stream().flatMapToInt(
                        productItem -> IntStream.of(
                                productItem.getPrice()
                                        * productItem.getCount()))
        ).sum();
    }

    private Integer getSelectedPrice(Cart cart, List<Long> cartProductIds) {
        return cart.getProducts().stream()
                .filter(product -> cartProductIds.contains(product.getId()))
                .flatMap(product -> product.getItems().stream())
                .mapToInt(item -> item.getPrice() * item.getCount())
                .sum();
    }
}
