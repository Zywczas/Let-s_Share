package com.zywczas.letsshare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zywczas.letsshare.models.local.UserLocal

@Dao
interface UserDao {

    @Query("DELETE FROM User")
    suspend fun clearTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserLocal)

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): UserLocal

}