package com.zywczas.letsshare.di.modules

import com.zywczas.letsshare.fragmentlogin.domain.LoginRepository
import com.zywczas.letsshare.fragmentlogin.domain.LoginRepositoryImpl
import com.zywczas.letsshare.fragmentfriends.domain.FriendsRepository
import com.zywczas.letsshare.fragmentfriends.domain.FriendsRepositoryImpl
import com.zywczas.letsshare.fragmentgroupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.fragmentgroupdetails.domain.GroupDetailsRepositoryImpl
import com.zywczas.letsshare.fragmentgroups.domain.GroupsRepository
import com.zywczas.letsshare.fragmentgroups.domain.GroupsRepositoryImpl
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
    abstract fun bindMainRepository(repo: FriendsRepositoryImpl): FriendsRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindGroupsRepository(repo: GroupsRepositoryImpl): GroupsRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindGroupDetailsRepository(repo: GroupDetailsRepositoryImpl): GroupDetailsRepository

}