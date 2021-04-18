package com.zywczas.letsshare.activitymain.domain

import com.zywczas.letsshare.model.User

interface SharedPrefsWrapper {

//    var isLoggedInLocally: Boolean //todo na razie nie uzywane

    var lastUsedEmail: String

    val userId: String
    val userName: String
    val userEmail: String

    var currentGroupId: String

    suspend fun saveUserLocally(user: User)

    suspend fun getLocalUser(): User

}