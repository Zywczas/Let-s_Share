package com.zywczas.letsshare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zywczas.letsshare.models.User

@Dao
interface UserDao {

    @Query("DELETE FROM user")
    suspend fun clearTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): User

}