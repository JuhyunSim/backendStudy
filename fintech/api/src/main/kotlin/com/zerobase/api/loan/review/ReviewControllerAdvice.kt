package com.zerobase.api.loan.review

import com.zerobase.api.loan.exception.CustomException
import com.zerobase.api.loan.exception.ErrorResponse
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [LoanReviewController::class])
class ReviewControllerAdvice {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(customException: CustomException)
    = ErrorResponse(customException).toResponseEntity()
}