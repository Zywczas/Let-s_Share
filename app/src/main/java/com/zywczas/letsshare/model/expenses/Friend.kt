package com.zywczas.letsshare.model.expenses

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Friend (
    val name: String = "",
    val email: String = ""
)