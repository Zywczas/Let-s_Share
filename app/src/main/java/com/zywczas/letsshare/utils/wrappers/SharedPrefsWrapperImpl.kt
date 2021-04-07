package com.zywczas.letsshare.utils.wrappers

import android.content.Context
import android.content.SharedPreferences
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.utils.wrappers.SharedPrefsWrapper
import javax.inject.Inject

class SharedPrefsWrapperImpl @Inject constructor (context: Context): SharedPrefsWrapper {

    private val prefsFileName = "com.zywczas.letsshare.prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFileName, 0)

    private val isLoggedInKey = "isLoggedInKey"
    private val userIdKey = "userIdKey"
    private val userNameKey = "userNameKey"
    private val userEmailKey = "userEmailKey"
    private val currentGroupIdKey = "currentGroupIdKey"

//    override var isLoggedInLocally: Boolean
//        get() = prefs.getBoolean(isLoggedInKey, false) //todo
//        set(value) = prefs.edit().putBoolean(isLoggedInKey, value).apply()

//    override val userAuthId: String = prefs.getString(userIdKey, "")!!
    override val userName: String = prefs.getString(userNameKey, "")!!
    override val userEmail: String = prefs.getString(userEmailKey, "")!!
    override var currentGroupId: String
        get() = prefs.getString(currentGroupIdKey, "")!!
        set(value) = prefs.edit().putString(currentGroupIdKey, value).apply()

    override suspend fun saveUserLocally(user: User) {
        prefs.edit().putString(userIdKey, user.auth_id).apply()
        prefs.edit().putString(userNameKey, user.name).apply()
        prefs.edit().putString(userEmailKey, user.email).apply()
    }

}