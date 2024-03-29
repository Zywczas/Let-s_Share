package com.zywczas.letsshare

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.zywczas.letsshare.db.UserDao
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.extentions.logD
import com.zywczas.letsshare.models.ExpenseNotification
import com.zywczas.letsshare.utils.wrappers.CrashlyticsWrapper
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import com.zywczas.letsshare.retrofitapi.NotificationRetrofitApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(
    context: Context,
    private val firebaseAuth: FirebaseAuth,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val messaging: FirebaseMessaging,
    private val firestoreRefs: FirestoreReferences,
    private val crashlytics: CrashlyticsWrapper,
    private val userDao: UserDao,
    private val notificationRetrofitApi: NotificationRetrofitApi
) : SessionManager {

    private var isConnected = false
    private var isLoggedIn = false

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isConnected = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isConnected = false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun registerNetworkCallback() {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override suspend fun isNetworkAvailable(): Boolean = isConnected

    override suspend fun isUserLoggedIn(): Boolean =
        if (isLoggedIn) {
            true
        } else {
            isLoggedIn = firebaseAuth.currentUser != null && firebaseAuth.currentUser!!.isEmailVerified
            isLoggedIn
        }

    override suspend fun logout() {
        firebaseAuth.signOut()
        isLoggedIn = false
    }

    override fun saveMessagingToken(newToken: String?) {
        GlobalScope.launch(dispatchersIO) {
            try {
                val token = newToken ?: messaging.token.await()
                val user = userDao.getUser()
                firestoreRefs.userRefs(user.id).update(firestoreRefs.messagingTokenField, token).await()
            } catch (e: Exception) {
                crashlytics.sendExceptionToFirebase(e)
                logD(e)
            }
        }
    }

    override fun wakeUpServer() {
        GlobalScope.launch(dispatchersIO) {
            try {
                notificationRetrofitApi.wakeUpTheServer()
            } catch (e: Exception) {
                logD("Waking up server: ${e.message}")
            }
        }
    }

    override fun sendNotification(notification: ExpenseNotification) {
        GlobalScope.launch(dispatchersIO) {
            try {
                notificationRetrofitApi.sendNotification(notification)
            } catch (e: Exception) {
                crashlytics.sendExceptionToFirebase(e)
                logD("'sendNotification' exception: ${e.message}")
            }
        }
    }

}