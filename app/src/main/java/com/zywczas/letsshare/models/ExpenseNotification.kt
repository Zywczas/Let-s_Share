package com.zywczas.letsshare.models

data class ExpenseNotification(
    val ownerName: String,
    val groupName: String,
    val receiversIds: List<String>
)