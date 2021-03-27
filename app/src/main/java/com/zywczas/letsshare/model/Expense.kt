package com.zywczas.letsshare.model

import com.zywczas.letsshare.utils.dateInPoland
import java.util.*

data class Expense (
    val id: String = "",
    val name: String = "",
    val payee_email: String = "",
    val payee_name: String = "",
    val value: String = "0.00",
    val date_created: Date = dateInPoland(),
)