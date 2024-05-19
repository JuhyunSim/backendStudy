package com.zerobase.css.service

import com.zerobase.css.dto.LoanReceiveDto
import com.zerobase.css.dto.LoanResult
import org.springframework.stereotype.Service

@Service
class LoanReviewService {
    fun loanReview(requestInputDto: LoanReceiveDto.RequestInputDto)
    : LoanResult.LoanReviewResponseDto{
        if (requestInputDto.userIncomeAmount < 0)
            throw RuntimeException("Invalid userIncomeAmount input")
        if (requestInputDto.userIncomeAmount < 10000000)
            return LoanResult.LoanReviewResponseDto(
                userKey = requestInputDto.userKey,
                userLimitAmount = 1000000,
                userLoanInterestRate = 5.0
            )
        if (requestInputDto.userIncomeAmount < 20000000)
            return LoanResult.LoanReviewResponseDto(
                userKey = requestInputDto.userKey,
                userLimitAmount = 2000000,
                userLoanInterestRate = 4.0
            )
        if (requestInputDto.userIncomeAmount < 30000000)
            return LoanResult.LoanReviewResponseDto(
                userKey = requestInputDto.userKey,
                userLimitAmount = 3000000,
                userLoanInterestRate = 3.0
            )
        if (requestInputDto.userIncomeAmount < 40000000)
            return LoanResult.LoanReviewResponseDto(
                userKey = requestInputDto.userKey,
                userLimitAmount = 4000000,
                userLoanInterestRate = 2.0
            )
        if (requestInputDto.userIncomeAmount >= 40000000)
            return LoanResult.LoanReviewResponseDto(
                userKey = requestInputDto.userKey,
                userLimitAmount = 5000000,
                userLoanInterestRate = 1.0
            )
        throw RuntimeException("Invalid userIncomeAmount input")
    }
}