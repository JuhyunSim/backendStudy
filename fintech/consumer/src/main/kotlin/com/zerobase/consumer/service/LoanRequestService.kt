package com.zerobase.consumer.service

import com.zerobase.com.zerobase.kafka.dto.KafkaLoanRequestDto
import com.zerobase.consumer.dto.LoanReviewResponseDto
import com.zerobase.domain.domain.LoanReview
import com.zerobase.domain.repository.LoanReviewRepository
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import java.time.Duration


@Service
class LoanRequestService(
    private val loanReviewRepository: LoanReviewRepository
) {

    companion object {
        const val cssUrl = "http://localhost:8081/css/api/v1/request"
    }

    fun loanRequest(loanRequestDto: KafkaLoanRequestDto) {
        //CB사에 응답 요청 -> 응답값을 DB에 저장

        val reviewResult = loanRequestToCb(loanRequestDto)
        saveLoanReview(reviewResult.toLoanReviewEntity())
    }

    fun loanRequestToCb(loanRequestDto: KafkaLoanRequestDto): LoanReviewResponseDto {
        val restTemplate = RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(1000))
            .setReadTimeout(Duration.ofMillis(1000))
            .build()
        return restTemplate.postForEntity(cssUrl, loanRequestDto, LoanReviewResponseDto::class.java).body!!
    }

    fun saveLoanReview(loanReview: LoanReview) = loanReviewRepository.save(loanReview)


}