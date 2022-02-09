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
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.MainActivity
import com.zywczas.letsshare.extentions.getColorFromAttr
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagingService : FirebaseMessagingService() {

    companion object {
        const val KEY_IS_FROM_EXPENSE_NOTIFY = "KEY_IS_FROM_EXPENSE_NOTIFY"
    }

    @Inject
    lateinit var sessionManager: SessionManager

    private val scope = CoroutineScope(Dispatchers.IO)

    private val ownerNameKey = "ownerName"
    private val groupNameKey = "groupName"

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onNewToken(token: String) {
        scope.launch {
            sessionManager.saveMessagingToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val expenseOwner = message.data[ownerNameKey]
        val groupName = message.data[groupNameKey]
        sendNewExpenseNotification(getString(R.string.new_expense_notification_title, expenseOwner), getString(R.string.new_expense_notification_message, groupName))
    }

    private fun sendNewExpenseNotification(title : String, message : String){
        val notifyIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(KEY_IS_FROM_EXPENSE_NOTIFY, true)
        }
        val notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, ChannelIds.NEW_EXPENSE)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(getColorFromAttr(R.attr.colorPrimary))
            .setLargeIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher_round)!!.toBitmap())
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(notifyPendingIntent)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SOCIAL)

        NotificationManagerCompat.from(this).notify(NotificationsIds.NEW_EXPENSE, builder.build())
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

}