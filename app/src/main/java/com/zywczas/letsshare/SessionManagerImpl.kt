package com.zywczas.letsshare

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.google.firebase.auth.FirebaseAuth
import com.zywczas.letsshare.utils.logD
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth
) : SessionManager {

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
        if (isLoggedIn) {
            true
        } else {
            isLoggedIn = firebaseAuth.currentUser != null
            logD("czy jest zalogowany?: $isLoggedIn ")
            isLoggedIn
        }


}