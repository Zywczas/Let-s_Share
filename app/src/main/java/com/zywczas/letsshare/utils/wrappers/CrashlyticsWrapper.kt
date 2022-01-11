package com.zywczas.letsshare.utils.wrappers

interface CrashlyticsWrapper {

    fun sendExceptionToFirebase(e: Exception, key: Pair<String, String>? = null, log: String? = null)
    
}