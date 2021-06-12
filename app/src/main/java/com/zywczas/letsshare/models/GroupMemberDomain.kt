package com.zywczas.letsshare.models

import androidx.annotation.StringRes
import java.math.BigDecimal

data class GroupMemberDomain (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val expenses: BigDecimal = BigDecimal.ZERO,
    var share: BigDecimal = BigDecimal("100.00"),
    @StringRes var owesOrOver: Int = android.R.string.unknownName,
    var difference: BigDecimal = BigDecimal.ZERO
)