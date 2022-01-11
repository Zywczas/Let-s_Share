package com.zywczas.letsshare.activitymain.domain

interface CrashlyticsWrapper {

    fun sendExceptionToFirebase(e: Exception, key: Pair<String, String>? = null, log: String? = null)
    
}

//todo przeniesc do utils