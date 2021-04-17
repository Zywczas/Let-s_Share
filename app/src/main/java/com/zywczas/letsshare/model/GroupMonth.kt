package com.zywczas.letsshare.model

import com.zywczas.letsshare.utils.dateInPoland
import java.util.*

data class GroupMonth (
    val id: String = "",
    val total_expenses: String = "0.00",
    val is_settled_up: Boolean = false,
    val date_created: Date = dateInPoland()
)