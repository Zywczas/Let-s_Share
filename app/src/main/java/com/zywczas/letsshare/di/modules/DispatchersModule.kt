package com.zywczas.letsshare.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(ActivityRetainedComponent::class)
class DispatchersModule {

    @Provides
    @DispatchersIO
    fun provideDispatchersIO() : CoroutineDispatcher = Dispatchers.IO

    @Qualifier
    annotation class DispatchersIO

}