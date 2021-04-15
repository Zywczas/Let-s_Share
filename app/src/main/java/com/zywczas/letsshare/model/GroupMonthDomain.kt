package com.zywczas.letsshare.model

import java.math.BigDecimal

data class GroupMonthDomain (
    val id: String = "",
    val groupId: String = "",
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val isSettledUp: Boolean = false
)