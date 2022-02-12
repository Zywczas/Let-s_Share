package com.zywczas.letsshare.retrofitapi

import com.zywczas.letsshare.models.ExpenseNotification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationRetrofitApi {

    /**
     * Simple function to wake up the Heroku server,
     * as it falls asleep after 30 minutes of being idle.
     */
    @GET("api/v1/server/wake_up")
    suspend fun wakeUpTheServer(): Response<MessageResponse>

    @POST("api/v1/notification/expense_notification")
    suspend fun sendNotification(@Body notification: ExpenseNotification): Response<MessageResponse>

}
