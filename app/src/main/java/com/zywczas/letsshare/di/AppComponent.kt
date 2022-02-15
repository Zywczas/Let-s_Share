package com.zywczas.letsshare.di

import android.app.Application
import com.zywczas.letsshare.BaseApp
import com.zywczas.letsshare.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    DispatchersModule::class,
    DomainModule::class,
    ActivityBuilderModule::class,
    FragmentFactoryModule::class,
    ViewModelFactoryModule::class,
    RoomModule::class,
    UtilsModule::class,
    NetworkModule::class,
    ServiceBuilderModule::class
])
interface AppComponent : AndroidInjector<BaseApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

}