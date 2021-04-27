package com.zywczas.letsshare.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class GroupMonthDomain (
    val id: String = "",
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val isSettledUp: Boolean = false
) : Parcelable