package com.zywczas.letsshare.di.modules

import androidx.fragment.app.Fragment
import com.zywczas.letsshare.di.qualifiers.FragmentKey
import com.zywczas.letsshare.fragmentlogin.presentation.LoginFragment
import com.zywczas.letsshare.fragmentfriends.presentation.FriendsFragment
import com.zywczas.letsshare.fragmentgroups.presentation.GroupsFragment
import com.zywczas.letsshare.fragmentregister.presentation.RegisterFragment
import com.zywczas.letsshare.fragmentwelcome.presentation.WelcomeFragment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class)
abstract class FragmentFactoryModule {

    @Binds
    @IntoMap
    @FragmentKey(WelcomeFragment::class)
    abstract fun bindWelcomeFragment(fragment: WelcomeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    abstract fun bindLoginFragment(fragment: LoginFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RegisterFragment::class)
    abstract fun bindRegisterFragment(fragment: RegisterFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FriendsFragment::class)
    abstract fun bindMainFragment(fragment: FriendsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(GroupsFragment::class)
    abstract fun bindGroupsFragment(fragment: GroupsFragment): Fragment
}