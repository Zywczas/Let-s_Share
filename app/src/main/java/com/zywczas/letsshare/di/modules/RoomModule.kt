package com.zywczas.letsshare.di.modules

import android.content.Context
import androidx.room.Room
import com.zywczas.letsshare.db.AppDataBase
import com.zywczas.letsshare.db.FriendsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDataBase(context: Context): AppDataBase =
        Room.databaseBuilder(context, AppDataBase::class.java, "AppDataBase").build()

    @Provides
    fun provideFriendsDao(db: AppDataBase): FriendsDao = db.friendsDao()

}