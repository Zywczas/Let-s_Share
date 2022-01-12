package com.zywczas.letsshare.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserLocal(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "groupsIds") val groupsIds: List<String> = emptyList(),
    @ColumnInfo(name = "messagingToken") val messagingToken: String = ""
)