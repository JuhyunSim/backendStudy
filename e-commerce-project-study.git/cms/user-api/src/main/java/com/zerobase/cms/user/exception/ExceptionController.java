package com.zerobase.cms.user.exception;

import jakarta.servlet.ServletException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ExceptionResponse> handleCustomException(final CustomException c) {
        log.warn("api Exception: {} ", c.getMessage());
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(c.getErrorCode(), c.getMessage())
        );
    }

//    @ExceptionHandler({ServletException.class})
//    public ResponseEntity<String> handleServletException(String errorMessage) {
//        log.warn("api Exception: {} ", errorMessage);
//        return ResponseEntity.badRequest().body("잘못된 인증 시도.");
//    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class ExceptionResponse {
        private ErrorCode errorCode;
        private String errorMessage;
    }
}
