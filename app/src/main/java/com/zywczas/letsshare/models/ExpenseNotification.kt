package com.zywczas.letsshare.models

import com.google.gson.annotations.SerializedName

data class ExpenseNotification(
    @SerializedName("ownerName")
    val ownerName: String,

    @SerializedName("groupName")
    val groupName: String,

    @SerializedName("receiversIds")
    val receiversIds: List<String>
)