package com.zerobase.cms.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    WRONG_VERIFICATION(HttpStatus.BAD_REQUEST, "잘못된 인증시도입니다."),
    EXPIRED_CODE(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),

    //상품
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    NOT_FOUND_ITEM(HttpStatus.BAD_REQUEST, "아이템을 찾을 수 없습니다."),
    SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "아이템 명 중복입니다."),
    ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품의 수량이 부족합니다."),

    //장바구니
    CART_CHANGE_FAIL(HttpStatus.BAD_REQUEST, "장바구니에 추가하실 수 없습니다."),

    //주문
    ORDER_FAIL_CHECK_CART(HttpStatus.BAD_REQUEST, "주문 불가! 장바구니를 확인하세요"),
    ORDER_FAIL_NOT_ENOUGH_BALANCE(HttpStatus.BAD_REQUEST, "주문 불가! 잔액이 부족합니다."),

    //login
    LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "로그인 이메일이나 패스워드를 확인해주세요."),

    ALREADY_VERIFIED_USER(HttpStatus.BAD_REQUEST, "이미 인증이 완료되었습니다.");


    private final HttpStatus httpStatus;
    private final String errorMessage;
}
