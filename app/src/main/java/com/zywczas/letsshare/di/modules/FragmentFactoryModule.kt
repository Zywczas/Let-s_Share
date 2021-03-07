package com.zywczas.letsshare.di.modules

import androidx.fragment.app.Fragment
import com.zywczas.letsshare.di.qualifiers.FragmentKey
import com.zywczas.letsshare.fragmenthome.presentation.HomeFragment
import com.zywczas.letsshare.fragmentlogin.presentation.LoginFragment
import com.zywczas.letsshare.fragmentmain.presentation.MainFragment
import com.zywczas.letsshare.fragmentregister.presentation.RegisterFragment
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
    @FragmentKey(HomeFragment::class)
    abstract fun bindHomeFragment(fragment: HomeFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    abstract fun bindLoginFragment(fragment: LoginFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(RegisterFragment::class)
    abstract fun bindRegisterFragment(fragment: RegisterFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(MainFragment::class)
    abstract fun bindMainFragment(fragment: MainFragment) : Fragment

}