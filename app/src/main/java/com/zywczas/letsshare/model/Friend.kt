package com.zywczas.letsshare.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
data class Friend (

    @PrimaryKey
    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "name")
    val name: String = ""

)