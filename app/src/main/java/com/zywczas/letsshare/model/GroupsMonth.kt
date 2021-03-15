package com.zywczas.letsshare.model

data class GroupsMonth (
    val id: String = "",
    val group_id: String = "",
    var total_expenses: Double = 0.0,
    var is_closed: Boolean = false,
    var is_settled_up: Boolean = false
)