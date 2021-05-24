package com.zywczas.letsshare.webservices

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("message")
    val message: String
)