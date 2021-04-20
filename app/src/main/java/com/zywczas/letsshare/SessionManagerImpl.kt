package com.zywczas.letsshare

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth) : SessionManager {

    private var isConnected = false
    private var isLoggedIn = false

    init {
        registerNetworkCallback()
    }

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
        //todo pytanie czy tu trzeba tez zapisywac za kazdym razem przy wlczaniu aplikacji, dane do shared prefs czy nie trzeba, stad mozna wziac email, display name i id
        else {
            isLoggedIn = firebaseAuth.currentUser != null && firebaseAuth.currentUser!!.isEmailVerified
            isLoggedIn
        }

    override suspend fun delayCoroutine(millis: Long) = delay(millis)

    override suspend fun logout() = firebaseAuth.signOut()

}