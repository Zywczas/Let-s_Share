package com.zywczas.letsshare

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SessionManager {

    private var isConnected = false

    private var isLoggedIn = true //todo pozniej zamienic na false i dac sprawdzenie w funkcji ponizej

    init {
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val cm =
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
            //todo to na razie sprawdza tylko fejsa, ale trzeba bedzie dodac sprawdzanie np z google albo maila
//            val token = AccessToken.getCurrentAccessToken()
//            isLoggedIn = token != null && token.isExpired.not()
            isLoggedIn
        }

}