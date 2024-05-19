package com.zerobase.api.loan.exception

import org.springframework.http.HttpStatus

enum class CustomErrorCode(
    val errorStatus: HttpStatus,
    val errorCode: String,
    val errorMessage: String,
) {
    RESULT_NOT_FOUND(
        errorStatus = HttpStatus.BAD_REQUEST,
        errorCode = "E001",
        errorMessage = "result not found"
    )
}