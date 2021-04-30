package com.zywczas.letsshare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zywczas.letsshare.models.Friend

@Dao
interface FriendsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(friends: List<Friend>)

    @Query("SELECT * FROM friends ORDER BY name ASC")
    suspend fun getFriends(): List<Friend>

}