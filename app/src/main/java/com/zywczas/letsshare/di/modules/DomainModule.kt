package com.zywczas.letsshare.di.modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DomainModule {

//    @Binds
//    @ActivityRetainedScoped
//    abstract fun bindLoginRepository(repo: LoginRepositoryImpl) : LoginRepository

}