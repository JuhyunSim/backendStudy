package com.zerobase.css.dto

class LoanReceiveDto {
    data class RequestInputDto(
        val userKey: String,
        val userName: String,
        val userIncomeAmount: Long,
        var userRegistrationNumber: String
    )
}