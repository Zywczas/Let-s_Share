package com.zywczas.letsshare.di.modules

import com.zywczas.letsshare.webservices.NotificationService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    private val letsShareApiBaseUrl = "https://zywczas-lets-share.herokuapp.com"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(letsShareApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideNotificationService(retrofit: Retrofit): NotificationService = retrofit.create(NotificationService::class.java)

}