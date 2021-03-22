package com.zywczas.letsshare.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zywczas.letsshare.model.Friend

@Database(entities = [Friend::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun friendsDao(): FriendsDao

}