package com.zywczas.letsshare.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

data class User (
    val id: String,
    val name: String,
    val email: String,
    @ServerTimestamp
    val time_created: Date? = null
)