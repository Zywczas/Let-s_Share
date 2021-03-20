package com.zywczas.letsshare.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val id: String = "",
    val founder_email: String = "",
    val name: String = "",
    val date_created: String = "",
    val currency: String = "",
    var members_num: Int = 0
) : Parcelable