package com.zywczas.letsshare.activitymain.domain

import android.content.Context
import android.content.SharedPreferences
import com.zywczas.letsshare.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefsWrapperImpl @Inject constructor (@ApplicationContext context: Context): SharedPrefsWrapper {

    private val prefsFileName = "com.zywczas.letsshare.prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFileName, 0)

    private val isLoggedInKey = "isLoggedInKey"
    private val userIdKey = "userIdKey"
    private val userNameKey = "userNameKey"
    private val userEmailKey = "userEmailKey"

    override var isLoggedInLocally: Boolean
        get() = prefs.getBoolean(isLoggedInKey, false)
        set(value) = prefs.edit().putBoolean(isLoggedInKey, value).apply()

    private var userId: String
        get() = prefs.getString(userIdKey, "")!!
        set(value) = prefs.edit().putString(userIdKey, value).apply()

    private var userName: String
        get() = prefs.getString(userNameKey, "")!!
        set(value) = prefs.edit().putString(userNameKey, value).apply()

    override var userEmail: String
        get() = prefs.getString(userEmailKey, "")!!
        set(value) = prefs.edit().putString(userEmailKey, value).apply()

    override suspend fun saveUserLocally(user: User) {
        userId = user.auth_id
        userName = user.name
        userEmail = user.email
    }

}