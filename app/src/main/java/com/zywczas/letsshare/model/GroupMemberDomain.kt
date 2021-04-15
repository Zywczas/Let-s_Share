package com.zywczas.letsshare.model

import java.math.BigDecimal

data class GroupMemberDomain (
    val name: String = "",
    val email: String = "",
    val expenses: BigDecimal = BigDecimal.ZERO,
    var percentage_share: BigDecimal = BigDecimal("100.00"),
    var owes: String = "",
    var balance: BigDecimal = BigDecimal.ZERO
)