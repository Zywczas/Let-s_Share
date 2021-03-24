package com.zywczas.letsshare.utils.wrappers

interface CrashlyticsWrapper {

    suspend fun sendExceptionToFirebase(
        e: Exception,
        key: Pair<String, String>? = null,
        log: String? = null
    )
    
}