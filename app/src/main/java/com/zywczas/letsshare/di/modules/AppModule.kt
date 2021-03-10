package com.zywczas.letsshare.di.modules

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.SessionManagerImpl
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth): SessionManager = SessionManagerImpl(context, firebaseAuth)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideSharedPrefsWrapper(@ApplicationContext context: Context): SharedPrefsWrapper = SharedPrefsWrapperImpl(context)

}