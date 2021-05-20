package com.zywczas.letsshare

import androidx.lifecycle.LifecycleObserver

interface SessionManager : LifecycleObserver {

    suspend fun isNetworkAvailable() : Boolean

    suspend fun isUserLoggedIn() : Boolean

    suspend fun delayCoroutine(millis: Long)

    suspend fun logout()

    fun saveMessagingToken()

    fun sendNotification()

}