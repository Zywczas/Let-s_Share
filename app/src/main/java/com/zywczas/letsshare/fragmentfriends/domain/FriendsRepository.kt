package com.zywczas.letsshare.fragmentfriends.domain

import com.zywczas.letsshare.model.Friend

interface FriendsRepository {

    suspend fun logout()
    
    suspend fun getFriends(): List<Friend>?

    suspend fun addFriendByEmail(email: String, onFinishAction: (Int) -> Unit )

}