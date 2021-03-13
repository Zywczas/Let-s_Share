package com.zywczas.letsshare.model.expenses

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val time_created: String = ""
)