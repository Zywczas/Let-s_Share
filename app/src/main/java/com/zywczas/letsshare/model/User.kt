package com.zywczas.letsshare.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val groupsIds: List<String> = emptyList()
)