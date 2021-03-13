package com.zywczas.letsshare.activitymain.domain

import com.zywczas.letsshare.model.expenses.User

interface SharedPrefsWrapper {

    var isLoggedInLocally: Boolean //todo na razie nie uzywane
    var userEmail: String

    suspend fun saveUserLocally(user: User)

}