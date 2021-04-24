package com.zywczas.letsshare.di.modules

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class UtilsModule {

    @Provides
    @TextDebounce
    fun provideTextDebounce(): Long = 500L

    @Qualifier
    annotation class TextDebounce

}