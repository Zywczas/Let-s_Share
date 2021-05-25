package com.zywczas.letsshare.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.MainActivity
import com.zywczas.letsshare.utils.EXPENSE_CHANNEL_ID
import com.zywczas.letsshare.utils.logD

class MessagingService : FirebaseMessagingService() {

    private val ownerNameKey = "ownerName"
    private val groupNameKey = "groupName"

    override fun onNewToken(token: String) {
        //dac zapisywanie tokena do bazy - aktualizowanie
        logD("nowy token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val expenseOwner = message.data[ownerNameKey]
        val groupName = message.data[groupNameKey]
        logD("nowy wydatek")
        logD("groupName: $groupName")
        logD("expenseOwner: $expenseOwner")
        sendBroadcastNotification(getString(R.string.new_expense_notification_title, expenseOwner), getString(R.string.new_expense_notification_message, groupName))
    }

    private fun sendBroadcastNotification(title : String, message : String){
        logD("sendBroadcastNotification")
        val notifyIntent = Intent(this, MainActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

//        notifyIntent.putExtra("klucz chatoroomu", "chatroom number") todo

        val notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val largeIcon = ContextCompat.getDrawable(this, R.mipmap.ic_launcher_round)!!.toBitmap()

        val builder = NotificationCompat.Builder(this, EXPENSE_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(ContextCompat.getColor(this, R.color.primary))
            .setLargeIcon(largeIcon)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(notifyPendingIntent)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SOCIAL)

        NotificationManagerCompat.from(this).notify(10001, builder.build())
    }

}