package com.zywczas.letsshare.model

data class GroupMember (
    val name: String = "",
    val email: String = "",
    val expenses: String = "0.00",
    val percentage_share: String = "100.00"
)