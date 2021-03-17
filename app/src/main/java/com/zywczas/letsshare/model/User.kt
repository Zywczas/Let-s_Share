package com.zywczas.letsshare.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User(
    val auth_id: String = "",
    val name: String = "",
    val email: String = "",
    val time_created: String = ""
)