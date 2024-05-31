package com.zerobase.cms.order.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private final int status;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public CustomException(ErrorCode errorCode, int status) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.status = this.errorCode.getHttpStatus().value();
    }
}
