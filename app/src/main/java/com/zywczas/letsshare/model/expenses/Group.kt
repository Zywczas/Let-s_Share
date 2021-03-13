package com.zywczas.letsshare.model.expenses

data class Group(
    val id: String = "",
    val founder_id: String = "",
    val name: String = "",
    val time_created: String = "",
    val currency: String = "",
    var members_num: Int = 0
)