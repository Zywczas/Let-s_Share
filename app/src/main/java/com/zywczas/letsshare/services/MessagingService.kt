package com.zywczas.letsshare.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zywczas.letsshare.utils.logD

class MessagingService : FirebaseMessagingService() {

    private val messageContent = "content"
    private val messageTitle = "title"

    //todo dac zapisywanie tokena do bazy - aktualizowanie
    override fun onNewToken(token: String) {
        logD("nowy token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        logD("wiadomosc otrzymana") //todo
        val notificationBody = message.notification?.body
        val notificationTitle = message.notification?.title
        val messageContent = message.data[messageContent]
        val messageTitle = message.data[messageTitle]
        logD("tytul powiadomienia: $notificationTitle")
        logD("cialo powiadomienia: $notificationBody")
        logD("tytul wiadomosci: $messageTitle") //Nowy wydatek
        logD("tresc wiadomosci: $messageContent") //Piotr dodał nowy wydatek do grupy Dom
    }

}