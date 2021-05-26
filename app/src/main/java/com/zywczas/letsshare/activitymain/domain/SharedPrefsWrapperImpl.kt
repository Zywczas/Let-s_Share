package com.zywczas.letsshare.activitymain.domain

import android.content.Context
import android.content.SharedPreferences
import com.zywczas.letsshare.models.User
import javax.inject.Inject

class SharedPrefsWrapperImpl @Inject constructor (context: Context): SharedPrefsWrapper {

    private val prefsFileName = "com.zywczas.letsshare.prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFileName, 0)

    private val lastUsedEmailKey = "lastUsedEmailKey"
    private val currentGroupIdKey = "currentGroupIdKey"

    override var lastUsedEmail: String
        set(value) = prefs.edit().putString(lastUsedEmailKey, value).apply()
        get() =  prefs.getString(lastUsedEmailKey, "")!!

    override var currentGroupId: String
        get() = prefs.getString(currentGroupIdKey, "")!!
        set(value) = prefs.edit().putString(currentGroupIdKey, value).apply()

}