package com.zywczas.letsshare.webservices

import com.zywczas.letsshare.models.ExpenseNotification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {

    @POST("api/v1/notification/expense_notification")
    suspend fun sendNotification(@Body notification: ExpenseNotification): Response<ApiResponse>

}
