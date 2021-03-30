package com.zywczas.letsshare.utils.wrappers

import com.zywczas.letsshare.model.User

interface SharedPrefsWrapper {

//    var isLoggedInLocally: Boolean //todo na razie nie uzywane

//    val userAuthId: String
    val userName: String
    val userEmail: String

    suspend fun saveUserLocally(user: User)

}