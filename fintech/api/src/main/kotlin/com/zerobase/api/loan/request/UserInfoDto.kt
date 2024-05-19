package com.zerobase.api.loan.request

import com.zerobase.domain.domain.UserInfo
import com.zerobase.com.zerobase.kafka.dto.KafkaLoanRequestDto

class UserInfoDto (
    val userName: String,
    val userKey: String,
    val userRegistrationNumber: String,
    val userIncomeAmount: Long
){
    fun toEntity() : UserInfo {
        return UserInfo(
            userKey = userKey,
            userName = userName,
            userRegistrationNumber = userRegistrationNumber,
            userIncomeAmount = userIncomeAmount
        )
    }

    fun toKafkaLoanRequestDto() = KafkaLoanRequestDto(
        userKey = userKey,
        userName = userName,
        userIncomeAmount = userIncomeAmount,
        userRegistrationNumber = userRegistrationNumber
    )
}