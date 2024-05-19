package com.zerobase.api.loan.request

import com.zerobase.api.loan.GenerateKey
import com.zerobase.api.loan.encrypt.EncryptComponent
import com.zerobase.com.zerobase.kafka.enum.KafkaTopics
import com.zerobase.com.zerobase.kafka.producer.LoanRequestSender
import com.zerobase.domain.repository.UserInfoRepository
import org.springframework.stereotype.Service

@Service
class LoanRequestServiceImpl(
    private val generateKey: GenerateKey,
    private val userInfoRepository: UserInfoRepository,
    private val encryptComponent: EncryptComponent,
    private val loanRequestSender: LoanRequestSender,
) : LoanRequestService {
    override fun loanRequestMain(
        loanRequestInputDto: LoanRequestDto.LoanRequestInputDto
    ) : LoanRequestDto.LoanRequestResponse {

        loanRequestInputDto.userRegistrationNumber = encryptComponent.encryptString(loanRequestInputDto.userRegistrationNumber )

        val userKey = generateKey.generateUserKey()

        val userInfoDto = loanRequestInputDto.toUserInfoDto(userKey)

        saveUserInfo(userInfoDto)

        loanRequestReview(userInfoDto)

         return LoanRequestDto.LoanRequestResponse(userKey)
    }

    override fun saveUserInfo(userInfoDto: UserInfoDto)
    = userInfoRepository.save(userInfoDto.toEntity())

    override fun loanRequestReview(userInfoDto: UserInfoDto) {
        loanRequestSender.sendLoanRequest(
            KafkaTopics.LOAN_REQUEST,
            userInfoDto.toKafkaLoanRequestDto()
        )
    }
}