package com.zywczas.letsshare.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.MainActivity
import com.zywczas.letsshare.utils.logD

class MessagingService : FirebaseMessagingService() {

    private val ownerNameKey = "ownerName"
    private val groupNameKey = "groupName"

    //todo dac zapisywanie tokena do bazy - aktualizowanie
    override fun onNewToken(token: String) {
        logD("nowy token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val expenseOwner = message.data[ownerNameKey]
        val groupName = message.data[groupNameKey]
        logD("nowy wydatek") //todo
        sendBroadcastNotification(getString(R.string.new_expense_notification_title, expenseOwner), getString(R.string.new_expense_notification_message, groupName))
    }

    private fun sendBroadcastNotification(title : String, message : String){
        val builder = NotificationCompat.Builder(this, "com.zywczas.letsshare.default_notification_channel_name")
        val notifyIntent = Intent(this, MainActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        notifyIntent.putExtra("klucz chatoroomu", "chatroom number") - to mozna uzyc do przekierowania do odpowiedniej wiadomosci, i wtedy przeskakujemy od razu //todo
        //na drugie activity, odpowiednie dla danego chatroomu, lepiej zeby w stacku byly 2 activity a nie tylko od razu odpowiedni chatroom, zeby uzytkwnik jak wcisnie
        //guzik wstecz to go prekierowalo do glownego okna
        //creates the pending intent
        val notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setSmallIcon(R.drawable.ic_add_expense)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(this, R.color.primary))
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        builder.setContentIntent(notifyPendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(10001, builder.build())
    }

}