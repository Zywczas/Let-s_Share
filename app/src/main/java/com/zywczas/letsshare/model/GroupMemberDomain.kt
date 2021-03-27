package com.zywczas.letsshare.model

import java.math.BigDecimal

data class GroupMemberDomain (
    val name: String = "",
    val email: String = "",
    val expenses: BigDecimal = BigDecimal.ZERO,
    val percentage_share: BigDecimal = BigDecimal.ZERO
)