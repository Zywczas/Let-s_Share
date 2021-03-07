package com.zywczas.letsshare

interface SessionManager {

    suspend fun isNetworkAvailable() : Boolean

    suspend fun isUserLoggedIn() : Boolean

    suspend fun sleep(millis: Long)

}