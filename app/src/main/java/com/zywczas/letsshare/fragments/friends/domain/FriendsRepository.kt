package com.zywczas.letsshare.fragments.friends.domain

import com.zywczas.letsshare.model.Friend
import kotlinx.coroutines.flow.Flow

interface FriendsRepository {

    suspend fun getFriends(): Flow<List<Friend>>

    suspend fun saveFriendsLocally(friends: List<Friend>)

    suspend fun userEmail(): String

    suspend fun addFriend(email: String): Int?

}