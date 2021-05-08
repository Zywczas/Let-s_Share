package com.zywczas.letsshare.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zywczas.letsshare.utils.logD

class MessagingService : FirebaseMessagingService() {

    init {
        logD("service startuje") //todo
    }

    //todo dac zapisywanie tokena do bazy - aktualizowanie
    override fun onNewToken(token: String) {
        super.onNewToken(token)


    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }

}