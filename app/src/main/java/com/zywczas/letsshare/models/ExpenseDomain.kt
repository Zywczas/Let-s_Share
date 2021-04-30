package com.zywczas.letsshare.models

import java.math.BigDecimal

data class ExpenseDomain (
    val id: String = "",
    val name: String = "",
    val payeeEmail: String = "",
    val payeeName: String = "",
    val value: BigDecimal = BigDecimal.ZERO,
    val dateCreated: String = "",
)