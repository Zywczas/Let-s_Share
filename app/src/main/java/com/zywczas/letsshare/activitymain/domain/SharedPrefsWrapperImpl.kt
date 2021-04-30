package com.zywczas.letsshare.activitymain.domain

import android.content.Context
import android.content.SharedPreferences
import com.zywczas.letsshare.models.User
import javax.inject.Inject

class SharedPrefsWrapperImpl @Inject constructor (context: Context): SharedPrefsWrapper {

    private val prefsFileName = "com.zywczas.letsshare.prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFileName, 0)

    private val lastUsedEmailKey = "lastUsedEmailKey"
    private val userIdKey = "userIdKey"
    private val userNameKey = "userNameKey"
    private val userEmailKey = "userEmailKey"
    private val currentGroupIdKey = "currentGroupIdKey"

    override var lastUsedEmail: String
        set(value) = prefs.edit().putString(lastUsedEmailKey, value).apply()
        get() =  prefs.getString(lastUsedEmailKey, "")!!

    override val userId: String = prefs.getString(userIdKey, "")!!
    override val userName: String = prefs.getString(userNameKey, "")!!
    override val userEmail: String = prefs.getString(userEmailKey, "")!!
    override var currentGroupId: String
        get() = prefs.getString(currentGroupIdKey, "")!!
        set(value) = prefs.edit().putString(currentGroupIdKey, value).apply()

    override suspend fun saveUserLocally(user: User) {
        prefs.edit().putString(userIdKey, user.id).apply()
        prefs.edit().putString(userNameKey, user.name).apply()
        prefs.edit().putString(userEmailKey, user.email).apply()
    }

    override suspend fun getLocalUser(): User = User(
        id = userId,
        name = userName,
        email = userEmail
    )

}