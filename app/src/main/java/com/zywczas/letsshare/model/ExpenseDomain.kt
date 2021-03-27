package com.zywczas.letsshare.model

import java.math.BigDecimal

data class ExpenseDomain (
    val id: String = "",
    val name: String = "",
    val payee_email: String = "",
    val payee_name: String = "",
    val value: BigDecimal = BigDecimal.ZERO,
    val date_created: String = "",
)