package com.zywczas.letsshare.models

import com.zywczas.letsshare.extentions.dateInPoland
import java.util.*

data class GroupMonth (
    val id: String = "",
    val totalExpenses: String = "0.00",
    @field:JvmField
    val isSettledUp: Boolean = false,
    val dateCreated: Date = dateInPoland()
)