package com.zywczas.letsshare.fragments.settings.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentSettingsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showSnackbar
import com.zywczas.letsshare.utils.turnOffOnBackPressed
import javax.inject.Inject

class SettingsFragment @Inject constructor(viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentSettingsBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        turnOffOnBackPressed()
        viewModel.getUser()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        binding.bottomNavBar.selectedItemId = R.id.settingsFragment
        setupObservers()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.isLoggedOut.observe(viewLifecycleOwner){
            if (it) findNavController().navigate(SettingsFragmentDirections.toWelcomeFragment())
        }
    }

    private fun setupOnClickListeners(){
        binding.logout.setOnClickListener { viewModel.logout() }
        binding.bottomNavBar.setOnNavigationItemSelectedListener(bottomNavClick)
    }

    private val bottomNavClick = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId){
            R.id.friendsFragment -> {
                findNavController().navigate(SettingsFragmentDirections.toFriendsFragment())
                true
            }
            R.id.groupsFragment -> {
                findNavController().navigate(SettingsFragmentDirections.toGroupsFragment())
                true
            }
            else -> false
        }
    }

}