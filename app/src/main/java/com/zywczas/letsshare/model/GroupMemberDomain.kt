package com.zywczas.letsshare.model

import java.math.BigDecimal

data class GroupMemberDomain (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val expenses: BigDecimal = BigDecimal.ZERO,
    var share: BigDecimal = BigDecimal("100.00"),
    var owesOrOver: Int = 0,
    var difference: BigDecimal = BigDecimal.ZERO
)