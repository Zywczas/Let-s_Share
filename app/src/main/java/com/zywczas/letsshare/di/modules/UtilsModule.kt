package com.zywczas.letsshare.di.modules

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class UtilsModule {

    @Provides
    @TextDebounce
    fun provideTextDebounce(): Long = 500L

    @Provides
    @WelcomeScreenDelay
    fun provideWelcomeScreenDelay() = 1500L

    @Qualifier
    annotation class TextDebounce

    @Qualifier
    annotation class WelcomeScreenDelay

}