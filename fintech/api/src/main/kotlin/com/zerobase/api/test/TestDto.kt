package com.zerobase.api.test

class TestDto {
    data class UserInfoDto(
        val userKey: String,
        val userName: String,
        val userRegistrationNumber: String,
        val userIncomeAmount: Long
    )
}