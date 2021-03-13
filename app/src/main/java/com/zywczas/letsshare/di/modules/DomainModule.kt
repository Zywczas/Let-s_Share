package com.zywczas.letsshare.di.modules

import com.zywczas.letsshare.fragmentlogin.domain.LoginRepository
import com.zywczas.letsshare.fragmentlogin.domain.LoginRepositoryImpl
import com.zywczas.letsshare.fragmentmain.domain.MainRepository
import com.zywczas.letsshare.fragmentmain.domain.MainRepositoryImpl
import com.zywczas.letsshare.fragmentregister.domain.RegisterRepository
import com.zywczas.letsshare.fragmentregister.domain.RegisterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DomainModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindLoginRepository(repo: LoginRepositoryImpl): LoginRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindRegisterRepository(repo: RegisterRepositoryImpl): RegisterRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindMainRepository(repo: MainRepositoryImpl): MainRepository

}