package com.zywczas.letsshare.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Friend (
    val name: String = "",
    val email: String = "",
    @ServerTimestamp
    val time_created: Date? = null
    )