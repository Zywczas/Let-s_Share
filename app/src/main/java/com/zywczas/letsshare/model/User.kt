package com.zywczas.letsshare.model

import com.zywczas.letsshare.utils.dateInPoland
import java.util.*

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val dateCreated: Date = dateInPoland(),
    val groupsIds: List<String> = emptyList()
)