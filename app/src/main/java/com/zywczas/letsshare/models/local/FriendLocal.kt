package com.zywczas.letsshare.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Friend")
data class FriendLocal (
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "name") val name: String = ""
)