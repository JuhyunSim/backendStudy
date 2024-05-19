package com.zerobase.api.loan.exception


class CustomException(val customErrorCode: CustomErrorCode) : RuntimeException()