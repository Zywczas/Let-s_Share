package com.zywczas.letsshare.model

import android.os.Parcelable
import com.zywczas.letsshare.utils.dateInPoland
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Group(
    val id: String = "",
    val founder_email: String = "",
    val name: String = "",
    val date_created: Date = dateInPoland(),
    val currency: String = "",
    var members_num: Int = 0
) : Parcelable