package com.zywczas.letsshare.di.modules

import com.zywczas.letsshare.retrofitapi.NotificationRetrofitApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    private val letsShareApiBaseUrl = "https://zywczas-lets-share.herokuapp.com"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(letsShareApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideNotificationRetrofitApi(retrofit: Retrofit): NotificationRetrofitApi = retrofit.create(NotificationRetrofitApi::class.java)

}