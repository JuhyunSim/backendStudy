package com.zerobase.css.dto

class LoanResult {

    data class LoanReviewResponseDto(
        val userKey: String,
        val userLimitAmount: Long,
        val userLoanInterestRate: Double
    )
}