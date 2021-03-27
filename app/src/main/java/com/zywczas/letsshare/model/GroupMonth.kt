package com.zywczas.letsshare.model

data class GroupMonth (
    val id: String = "",
    val group_id: String = "",
    var total_expenses: String = "0.00",
    var is_closed: Boolean = false,
    var is_settled_up: Boolean = false
)