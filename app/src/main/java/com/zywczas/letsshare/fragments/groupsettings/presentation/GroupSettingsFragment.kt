package com.zywczas.letsshare.fragments.groupsettings.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentGroupSettingsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.fragments.groupdetails.adapters.GroupMembersAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showToast
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupSettingsFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: GroupSettingsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupSettingsBinding by autoRelease()
    private val args: GroupSettingsFragmentArgs by navArgs()
    private val membersAdapter by lazy { GroupMembersAdapter(args.currency) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch { viewModel.getMembers() }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            membersAdapterXML = membersAdapter
        }
        setupToolbar()
        setupObservers()
        setupSpeedDialMenu()
        setupOnClickListeners()
    }

    private fun setupToolbar(){
        binding.toolbar.setupWithNavController(findNavController())
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
        viewModel.members.observe(viewLifecycleOwner){ membersAdapter.submitList(it.toMutableList()) }
    }

    private fun setupSpeedDialMenu(){

    }

    private fun setupOnClickListeners(){

    }

}