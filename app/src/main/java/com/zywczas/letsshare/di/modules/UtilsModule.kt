package com.zywczas.letsshare.di.modules

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class UtilsModule {

    @Provides
    @TextListenerDebounce
    fun provideTextListenerDebounce(): Long = 500L

    @Provides
    @WelcomeScreenDelay
    fun provideWelcomeScreenDelay() = 1500L

    @Qualifier
    annotation class TextListenerDebounce

    @Qualifier
    annotation class WelcomeScreenDelay

}