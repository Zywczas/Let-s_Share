package com.zywczas.letsshare.utils.wrappers

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class CrashlyticsWrapperImpl @Inject constructor(
    private val crashlytics: FirebaseCrashlytics,
    private val sharedPrefs: SharedPrefsWrapper
) : CrashlyticsWrapper {

    override suspend fun sendExceptionToFirebase(e: Exception, key: Pair<String, String>?, log: String?){
        crashlytics.setUserId(sharedPrefs.userEmail)
        key?.let { crashlytics.setCustomKey(it.first, it.second) }
        log?.let { crashlytics.log(it) }
        crashlytics.recordException(e)
    }

}