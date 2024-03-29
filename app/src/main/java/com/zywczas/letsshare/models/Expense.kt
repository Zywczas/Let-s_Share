package com.zywczas.letsshare.models

import java.math.BigDecimal

data class Expense (
    val id: String = "",
    val name: String = "",
    val payeeId: String = "",
    val payeeName: String = "",
    val value: BigDecimal = BigDecimal.ZERO,
    val dateCreated: String = "",
)