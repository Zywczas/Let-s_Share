package com.zywczas.letsshare.di.modules

import androidx.lifecycle.ViewModel
import com.zywczas.letsshare.di.qualifiers.ViewModelKey
import com.zywczas.letsshare.fragments.friends.presentation.FriendsViewModel
import com.zywczas.letsshare.fragments.groupdetails.presentation.GroupDetailsViewModel
import com.zywczas.letsshare.fragments.groups.presentation.GroupsViewModel
import com.zywczas.letsshare.fragments.login.presentation.LoginViewModel
import com.zywczas.letsshare.fragments.register.presentation.RegisterViewModel
import com.zywczas.letsshare.fragments.welcome.presentation.WelcomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(vm: WelcomeViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(vm: LoginViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(vm: RegisterViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendsViewModel::class)
    abstract fun bindFriendsViewModel(vm: FriendsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupsViewModel::class)
    abstract fun bindGroupsViewModel(vm: GroupsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupDetailsViewModel::class)
    abstract fun bindGroupDetailsViewModel(vm: GroupDetailsViewModel) : ViewModel

}