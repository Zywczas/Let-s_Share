package com.zywczas.letsshare

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.databinding.ObservableBoolean
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class SessionManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SessionManager {

    private var isConnected = false
    private var isLoggedIn = false

    private val firebaseAuth = Firebase.auth

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
        if (isLoggedIn) {
            true
        } else {
            isLoggedIn = firebaseAuth.currentUser != null
            isLoggedIn
        }

    //todo dodac fejsa
//            val token = AccessToken.getCurrentAccessToken()
//            isLoggedIn = token != null && token.isExpired.not()

}