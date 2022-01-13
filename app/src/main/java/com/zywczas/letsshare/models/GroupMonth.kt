package com.zywczas.letsshare.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class GroupMonth (
    val id: String = "",
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val isSettledUp: Boolean = false
) : Parcelable