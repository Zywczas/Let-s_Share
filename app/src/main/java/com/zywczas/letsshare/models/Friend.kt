package com.zywczas.letsshare.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
data class Friend (

    @PrimaryKey
    @ColumnInfo(name = "id") //todo pousuwac column info jak nie potrzeba
    val id: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "name")
    val name: String = ""

)