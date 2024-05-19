package com.zerobase.com.zerobase.kafka.dto

import kotlinx.serialization.Serializable

@Serializable
data class KafkaLoanRequestDto(
    val userKey: String,
    val userName: String,
    val userIncomeAmount: Long,
    var userRegistrationNumber: String
)
