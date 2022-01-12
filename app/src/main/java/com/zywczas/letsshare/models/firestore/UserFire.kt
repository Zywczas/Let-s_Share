package com.zywczas.letsshare.models.firestore

data class UserFire(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val groupsIds: List<String> = emptyList(),
    val messagingToken: String = ""
)