package com.zywczas.letsshare.di.modules

import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.SessionManagerImpl
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapperImpl
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapperImpl
import com.zywczas.letsshare.fragmentfriends.domain.FriendsRepository
import com.zywczas.letsshare.fragmentfriends.domain.FriendsRepositoryImpl
import com.zywczas.letsshare.fragmentgroupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.fragmentgroupdetails.domain.GroupDetailsRepositoryImpl
import com.zywczas.letsshare.fragmentgroups.domain.GroupsRepository
import com.zywczas.letsshare.fragmentgroups.domain.GroupsRepositoryImpl
import com.zywczas.letsshare.fragmentlogin.domain.LoginRepository
import com.zywczas.letsshare.fragmentlogin.domain.LoginRepositoryImpl
import com.zywczas.letsshare.fragmentregister.domain.RegisterRepository
import com.zywczas.letsshare.fragmentregister.domain.RegisterRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainModule {

    @Binds
    abstract fun bindSessionManager(repo: SessionManagerImpl): SessionManager

    @Binds
    abstract fun bindSharedPrefsWrapper(repo: SharedPrefsWrapperImpl): SharedPrefsWrapper

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