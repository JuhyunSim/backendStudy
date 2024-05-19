package com.zerobase.consumer.kafka

import com.zerobase.com.zerobase.kafka.dto.KafkaLoanRequestDto
import com.zerobase.consumer.service.LoanRequestService
import kotlinx.serialization.json.Json
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class LoanRequestConsumer(
    private val loanRequestService: LoanRequestService,
    requestService: LoanRequestService
) {

    @KafkaListener(topics = ["loan_request"], groupId = "fintech")
    fun loanRequestTopicConsumer(message: String) {
        val kafkaLoanRequestDto = Json.decodeFromString<KafkaLoanRequestDto>(message)

        loanRequestService.loanRequest(kafkaLoanRequestDto)
    }
}