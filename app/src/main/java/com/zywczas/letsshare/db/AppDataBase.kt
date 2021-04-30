package com.zywczas.letsshare.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zywczas.letsshare.models.Friend

@Database(entities = [Friend::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun friendsDao(): FriendsDao

}