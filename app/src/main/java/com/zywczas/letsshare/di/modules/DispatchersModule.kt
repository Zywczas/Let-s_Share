package com.zywczas.letsshare.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
class DispatchersModule {

    @Provides
    @DispatchersIO
    fun provideDispatchersIO() : CoroutineDispatcher = Dispatchers.IO

    @Qualifier
    annotation class DispatchersIO

}