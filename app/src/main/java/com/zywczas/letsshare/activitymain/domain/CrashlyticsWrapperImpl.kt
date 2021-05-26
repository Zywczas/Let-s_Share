package com.zywczas.letsshare.activitymain.domain

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.zywczas.letsshare.db.UserDao
import com.zywczas.letsshare.di.modules.DispatchersModule
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CrashlyticsWrapperImpl @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val crashlytics: FirebaseCrashlytics,
    private val userDao: UserDao
) : CrashlyticsWrapper {

    override fun sendExceptionToFirebase(e: Exception, key: Pair<String, String>?, log: String?){
        GlobalScope.launch(dispatchersIO){
            crashlytics.setUserId(userDao.getUser().email)
            key?.let { crashlytics.setCustomKey(it.first, it.second) }
            log?.let { crashlytics.log(it) }
            crashlytics.recordException(e)
        }
    }

}