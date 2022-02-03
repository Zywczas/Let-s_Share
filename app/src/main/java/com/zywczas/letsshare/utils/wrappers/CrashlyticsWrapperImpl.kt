package com.zywczas.letsshare.utils.wrappers

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.zywczas.letsshare.db.UserDao
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CrashlyticsWrapperImpl @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val crashlytics: FirebaseCrashlytics,
    private val userDao: UserDao
) : CrashlyticsWrapper {

    override fun sendExceptionToFirebase(e: Exception, key: Pair<String, String>?, log: String?) {
        @Suppress("UNNECESSARY_SAFE_CALL", "RedundantNullableReturnType")
        //this is the only place where 'getUser()' can be null
        val userEmail: String? = userDao.getUser()?.email
        crashlytics.setUserId(userEmail ?: "")
        key?.let { crashlytics.setCustomKey(it.first, it.second) }
        log?.let { crashlytics.log(it) }
        crashlytics.recordException(e)
    }

}