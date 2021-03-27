package com.zywczas.letsshare.model

data class Expense (
    val id: String = "",
    val name: String = "",
    val payee_email: String = "",
    val payee_name: String = "",
    val value: String = "0.00",
    val date_created: String = "",
)