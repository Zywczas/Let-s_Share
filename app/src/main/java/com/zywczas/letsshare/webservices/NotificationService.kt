package com.zywczas.letsshare.webservices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationService {

    @POST("api/v1/notification/send_expense")
    suspend fun hello(@Body ): Response<Hello>

}