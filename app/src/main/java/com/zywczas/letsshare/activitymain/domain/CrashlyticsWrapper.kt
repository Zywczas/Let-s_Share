package com.zywczas.letsshare.activitymain.domain

import java.lang.Exception

interface CrashlyticsWrapper {

    suspend fun sendExceptionToFirebase(
        e: Exception,
        key: Pair<String, String>? = null,
        log: String? = null
    )
    
}