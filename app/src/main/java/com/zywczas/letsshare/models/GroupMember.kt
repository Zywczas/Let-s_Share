package com.zywczas.letsshare.models

data class GroupMember (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val expenses: String = "0.00",
    val share: String = "100.00"
)