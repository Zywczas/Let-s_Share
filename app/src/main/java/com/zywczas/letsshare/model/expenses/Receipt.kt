package com.zywczas.letsshare.model.expenses

data class Receipt (
    val id: String = "",
    val name: String = "",
    val payee_email: String = "",
    val payee_name: String = "",
    val value: Double = 0.0
)