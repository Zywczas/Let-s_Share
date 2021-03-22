package com.zywczas.letsshare.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.zywczas.letsshare.model.Friend

@Dao
interface FriendsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFriends(friends: List<Friend>)

}