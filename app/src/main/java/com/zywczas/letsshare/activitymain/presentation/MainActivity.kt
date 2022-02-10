package com.zywczas.letsshare.activitymain.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.factories.UniversalFragmentFactory
import com.zywczas.letsshare.services.ChannelIds
import com.zywczas.letsshare.services.MessagingService
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: UniversalFragmentFactory
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(sessionManager)
        goToGroupsFragment()
        sessionManager.wakeUpServer()
        createNotificationChannel()
    }

    private fun goToGroupsFragment() {
        val navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment).navController
        val isActivityOpenedFromExpenseNotification = intent.getBooleanExtra(MessagingService.KEY_IS_FROM_EXPENSE_NOTIFY, false)
        if (isActivityOpenedFromExpenseNotification){
            navController.navigate(R.id.groupsFragment)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.new_expense)
            val descriptionText = getString(R.string.new_expense_notification_info)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(ChannelIds.NEW_EXPENSE, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}