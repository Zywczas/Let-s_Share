package com.zywczas.letsshare.di.modules

import androidx.fragment.app.Fragment
import com.zywczas.letsshare.di.qualifiers.FragmentKey
import com.zywczas.letsshare.fragmentlogin.presentation.LoginFragment
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
    @FragmentKey(LoginFragment::class)
    abstract fun bindLoginFragment(fragment: LoginFragment) : Fragment


}