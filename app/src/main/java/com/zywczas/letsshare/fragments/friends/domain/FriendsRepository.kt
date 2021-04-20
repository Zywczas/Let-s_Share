package com.zywczas.letsshare.fragments.friends.domain

import com.zywczas.letsshare.model.Friend

interface FriendsRepository {

    suspend fun getFriends(): List<Friend>?

    suspend fun saveFriendsLocally(friends: List<Friend>)

    suspend fun userEmail(): String

    suspend fun addFriend(email: String): Int?

}