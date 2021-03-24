package com.zywczas.letsshare.di.modules

import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.SessionManagerImpl
import com.zywczas.letsshare.activitymain.domain.*
import com.zywczas.letsshare.fragments.friends.domain.FriendsRepository
import com.zywczas.letsshare.fragments.friends.domain.FriendsRepositoryImpl
import com.zywczas.letsshare.fragments.groupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.fragments.groupdetails.domain.GroupDetailsRepositoryImpl
import com.zywczas.letsshare.fragments.groups.domain.GroupsRepository
import com.zywczas.letsshare.fragments.groups.domain.GroupsRepositoryImpl
import com.zywczas.letsshare.fragments.login.domain.LoginRepository
import com.zywczas.letsshare.fragments.login.domain.LoginRepositoryImpl
import com.zywczas.letsshare.fragments.register.domain.RegisterRepository
import com.zywczas.letsshare.fragments.register.domain.RegisterRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class DomainModule {

    @Binds
    abstract fun bindSessionManager(repo: SessionManagerImpl): SessionManager

    @Binds
    abstract fun bindSharedPrefsWrapper(repo: SharedPrefsWrapperImpl): SharedPrefsWrapper

    @Binds
    abstract fun bindFirestoreWrapper(repo: FirestoreWrapperImpl): FirestoreWrapper

    @Binds
    abstract fun bindCrashlyticsWrapper(repo: CrashlyticsWrapperImpl): CrashlyticsWrapper

    @Binds
    abstract fun bindLoginRepository(repo: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindRegisterRepository(repo: RegisterRepositoryImpl): RegisterRepository

    @Binds
    abstract fun bindMainRepository(repo: FriendsRepositoryImpl): FriendsRepository

    @Binds
    abstract fun bindGroupsRepository(repo: GroupsRepositoryImpl): GroupsRepository

    @Binds
    abstract fun bindGroupDetailsRepository(repo: GroupDetailsRepositoryImpl): GroupDetailsRepository

}