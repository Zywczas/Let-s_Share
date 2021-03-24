package com.zywczas.letsshare.model

data class GroupMember (
    val name: String = "",
    val email: String = "",
    val expenses: Int = 0,
    val percentage_share: Double = 100.0
)