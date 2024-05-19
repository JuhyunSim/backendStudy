package com.zerobase.domain.repository

import com.zerobase.domain.domain.LoanReview
import com.zerobase.domain.domain.UserInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LoanReviewRepository : JpaRepository<LoanReview, Long> {
    fun findByUserKey(userKey: String) : LoanReview?
}