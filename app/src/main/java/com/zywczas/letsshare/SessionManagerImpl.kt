package com.zywczas.letsshare

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.db.UserDao
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val messaging: FirebaseMessaging,
    private val firestoreRefs: FirestoreReferences,
    private val crashlytics: CrashlyticsWrapper,
    private val userDao: UserDao
) : SessionManager {

    private var isConnected = false
    private var isLoggedIn = false

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun registerNetworkCallback() {
        val cm  =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected = false
            }
        })
    }

    override suspend fun isNetworkAvailable(): Boolean = isConnected

    override suspend fun isUserLoggedIn(): Boolean =
        if (isLoggedIn) { true }
        else {
            isLoggedIn = firebaseAuth.currentUser != null && firebaseAuth.currentUser!!.isEmailVerified
            isLoggedIn
        }

    override suspend fun delayCoroutine(millis: Long) = delay(millis)

    override suspend fun logout() {
        firebaseAuth.signOut()
        isLoggedIn = false
    }

    override fun saveMessagingToken() {
        GlobalScope.launch(dispatchersIO){
            try {
                val token = messaging.token.await()
                val user = userDao.getUser()
                firestoreRefs.userRefs(user.id).update(firestoreRefs.messagingTokenField, token).await()
            } catch (e: Exception){
                crashlytics.sendExceptionToFirebase(e)
                logD(e)
            }
        }
    }

    override fun sendNotification() {
        GlobalScope.launch(dispatchersIO){
            //todo
        }
    }

}