package com.zywczas.letsshare.di.modules

import com.zywczas.letsshare.services.MessagingService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeMessagingService(): MessagingService

}