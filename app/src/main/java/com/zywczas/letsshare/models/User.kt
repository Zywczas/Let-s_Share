package com.zywczas.letsshare.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val groupsIds: List<String> = emptyList(),
    val messagingToken: String = ""
)