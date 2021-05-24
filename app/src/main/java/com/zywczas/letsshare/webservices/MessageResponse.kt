package com.zywczas.letsshare.webservices

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message")
    val message: String
)