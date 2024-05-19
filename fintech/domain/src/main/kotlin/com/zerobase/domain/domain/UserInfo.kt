package com.zerobase.domain.domain

import jakarta.persistence.*

@Entity
@Table(name = "USR_INFO")
class UserInfo (

    @Column(name = "usr_key")
    var userKey: String,

    @Column(name = "usr_reg_num")
    var userRegistrationNumber: String,

    @Column(name = "usr_nm")
    var userName: String,

    @Column(name = "usr_icm_amt")
    var userIncomeAmount: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}