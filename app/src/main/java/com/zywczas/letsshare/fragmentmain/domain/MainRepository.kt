package com.zywczas.letsshare.fragmentmain.domain

import com.zywczas.letsshare.model.expenses.Friend

interface MainRepository {

    suspend fun logout()
    
    suspend fun getFriends(): List<Friend>?

    suspend fun addFriendByEmail(email: String, onFinishAction: (Int) -> Unit )

}