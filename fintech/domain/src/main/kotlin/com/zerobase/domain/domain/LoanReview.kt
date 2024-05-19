package com.zerobase.domain.domain

import jakarta.persistence.*

@Entity
@Table (name = "LOAN_REVIEW")
class LoanReview (
    @Column(name = "usr_key")
    var userKey: String,

    @Column(name = "loan_lmt_amt")
    var loanLimitAmount: Long,

    @Column(name = "loan_intrt")
    var loanInterestRate: Double
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
