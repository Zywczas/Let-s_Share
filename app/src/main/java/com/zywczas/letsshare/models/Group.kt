package com.zywczas.letsshare.models

import android.os.Parcelable
import com.zywczas.letsshare.extentions.dateInPoland
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Group(
    val id: String = "",
    val name: String = "",
    val dateCreated: Date = dateInPoland(),
    val currency: String = "",
    var membersNum: Int = 0
) : Parcelable