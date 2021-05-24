package com.zywczas.letsshare.di.modules

import com.zywczas.letsshare.utils.LETS_SHARE_API_BASE_URL
import com.zywczas.letsshare.webservices.NotificationService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(LETS_SHARE_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideNotificationService(retrofit: Retrofit): NotificationService = retrofit.create(NotificationService::class.java)

}