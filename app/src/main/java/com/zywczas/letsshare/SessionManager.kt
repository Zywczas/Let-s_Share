package com.zywczas.letsshare

import androidx.lifecycle.LifecycleObserver
import com.zywczas.letsshare.models.ExpenseNotification

interface SessionManager : LifecycleObserver {

    suspend fun isNetworkAvailable() : Boolean

    suspend fun isUserLoggedIn() : Boolean

    suspend fun logout()

    fun saveMessagingToken(newToken: String? = null)

    fun wakeUpServer()

    fun sendNotification(notification: ExpenseNotification)

}