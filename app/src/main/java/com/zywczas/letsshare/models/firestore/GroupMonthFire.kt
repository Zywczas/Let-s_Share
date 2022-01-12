package com.zywczas.letsshare.models.firestore

import com.zywczas.letsshare.extentions.dateInPoland
import java.util.*

data class GroupMonthFire (
    val id: String = "",
    val totalExpenses: String = "0.00",
    @field:JvmField
    val isSettledUp: Boolean = false,
    val dateCreated: Date = dateInPoland()
)