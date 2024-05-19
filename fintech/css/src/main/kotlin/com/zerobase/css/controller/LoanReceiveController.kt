package com.zerobase.css.controller

import com.zerobase.css.dto.LoanReceiveDto
import com.zerobase.css.dto.LoanResult
import com.zerobase.css.service.LoanReviewService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/css/api/v1")
class LoanReceiveController(
    private val loanReviewService: LoanReviewService
) {

    @PostMapping("/request")
    fun loanReceive(@RequestBody loanReceiveDto: LoanReceiveDto.RequestInputDto):
            LoanResult.LoanReviewResponseDto =
        loanReviewService.loanReview(loanReceiveDto)


}