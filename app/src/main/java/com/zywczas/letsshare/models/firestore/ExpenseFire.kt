package com.zywczas.letsshare.models.firestore

import com.zywczas.letsshare.extentions.dateInPoland
import java.util.*

data class ExpenseFire (
    val id: String = "",
    val name: String = "",
    val payeeId: String = "",
    val payeeName: String = "",
    val value: String = "0.00",
    val dateCreated: Date = dateInPoland(),
)