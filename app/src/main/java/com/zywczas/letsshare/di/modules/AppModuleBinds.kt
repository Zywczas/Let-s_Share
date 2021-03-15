package com.zywczas.letsshare.di.modules

import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.SessionManagerImpl
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModuleBinds {

    @Binds
    @Singleton
    abstract fun provideSessionManager(repo: SessionManagerImpl): SessionManager

    @Binds
    abstract fun provideSharedPrefsWrapper(repo: SharedPrefsWrapperImpl): SharedPrefsWrapper

}