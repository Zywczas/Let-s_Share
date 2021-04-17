package com.zywczas.letsshare.model

import com.zywczas.letsshare.utils.dateInPoland
import java.util.*

data class GroupMonth (
    val id: String = "",
    val totalExpenses: String = "0.00",
    @field:JvmField
    val isSettledUp: Boolean = false,
    val dateCreated: Date = dateInPoland()
)