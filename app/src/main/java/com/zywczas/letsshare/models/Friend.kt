package com.zywczas.letsshare.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Friend (
    @PrimaryKey
    val id: String = "",
    val email: String = "",
    val name: String = ""
)