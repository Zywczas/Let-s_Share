package com.zywczas.letsshare.model

import java.math.BigDecimal

data class GroupMemberDomain (
    val name: String = "",
    val email: String = "",
    val expenses: BigDecimal = BigDecimal.ZERO,
    var percentageShare: BigDecimal = BigDecimal("100.00"),
    var owesOrOver: Int = 0,
    var balance: BigDecimal = BigDecimal.ZERO
)