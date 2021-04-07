package com.zywczas.letsshare.fragments.groupsettings.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentGroupSettingsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class GroupSettingsFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private var binding: FragmentGroupSettingsBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

}