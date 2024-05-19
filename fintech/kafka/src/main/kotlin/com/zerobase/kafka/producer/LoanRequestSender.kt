package com.zerobase.com.zerobase.kafka.producer

import com.zerobase.com.zerobase.kafka.enum.KafkaTopics
import com.zerobase.com.zerobase.kafka.dto.KafkaLoanRequestDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class LoanRequestSender(
    val kafkaTemplate: KafkaTemplate<String, String>,
) {
    fun sendLoanRequest(kafkaTopics: KafkaTopics, kafkaLoanRequestDto: KafkaLoanRequestDto) {
        kafkaTemplate.send(kafkaTopics.topicName, Json.encodeToString(kafkaLoanRequestDto))
    }
}