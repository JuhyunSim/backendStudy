package com.zerobase.consumer.dto

import com.zerobase.domain.domain.LoanReview

data class LoanReviewResponseDto (
        val userKey: String,
        val userLimitAmount: Long,
        val userLoanInterestRate: Double
    ) {

    fun toLoanReviewEntity() : LoanReview = LoanReview(
        userKey = userKey,
        loanLimitAmount = userLimitAmount,
        loanInterestRate = userLoanInterestRate
    )

}


