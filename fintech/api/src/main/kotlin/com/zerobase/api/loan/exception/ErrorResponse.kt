package com.zerobase.api.loan.exception

import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

class ErrorResponse(
    val customException: CustomException
) {
    fun toResponseEntity(): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity
            .status(customException.customErrorCode.errorStatus)
            .body(
                ErrorResponseDto(
                    errorCode = customException.customErrorCode.errorCode,
                    errorMessage = customException.customErrorCode.errorMessage
                )
            )
    }

    data class ErrorResponseDto(
        val errorCode: String,
        val errorMessage: String
    ) {
        fun timeStamp() = LocalDateTime.now()
    }
}