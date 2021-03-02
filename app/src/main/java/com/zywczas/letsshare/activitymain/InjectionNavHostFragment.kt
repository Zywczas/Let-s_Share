package com.zywczas.letsshare.activitymain

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import com.zywczas.letsshare.di.factories.UniversalFragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InjectionNavHostFragment : NavHostFragment() {

    @Inject
    lateinit var fragmentFactory : UniversalFragmentFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = fragmentFactory
    }

}