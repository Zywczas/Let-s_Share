package com.zywczas.letsshare.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val groupsIds: List<String> = emptyList(),
    val messagingToken: String = ""
)