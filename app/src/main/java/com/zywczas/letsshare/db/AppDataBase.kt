package com.zywczas.letsshare.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zywczas.letsshare.models.Friend
import com.zywczas.letsshare.models.User

@Database(
    entities = [
        User::class,
        Friend::class
               ],
    version = 2)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun friendsDao(): FriendsDao

}