package com.zywczas.letsshare.fragments.groups.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentGroupsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.fragments.groups.adapter.GroupsAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.hideSoftKeyboard
import com.zywczas.letsshare.utils.showToast
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupsFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: GroupsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupsBinding by autoRelease()
    private val groupsAdapter by lazy { GroupsAdapter{ lifecycleScope.launchWhenResumed {
        viewModel.saveCurrentlyOpenGroupId(it.id)
        findNavController().navigate(GroupsFragmentDirections.toGroupDetailsFragment(it)) //todo pozniej tutaj dac samo currency
    }}}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch { viewModel.getGroups() }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapter = groupsAdapter
        }
        binding.toolbar.setTitle(R.string.groups)
        setupObservers()
        setupOnClickListeners()
        setupBottomNavBar()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it)}
        viewModel.groups.observe(viewLifecycleOwner){ groupsAdapter.submitList(it.toMutableList()) }
    }

    private fun setupOnClickListeners(){
        binding.addGroup.setOnClickListener { lifecycleScope.launchWhenResumed {
            hideSoftKeyboard()
            viewModel.addGroup(binding.groupName.text.toString(), "zł") //todo pozniej dac rozne waluty
        }}
    }

    private fun setupBottomNavBar(){
        binding.bottomNavBar.setupWithNavController(findNavController())
        binding.bottomNavBar.setOnNavigationItemReselectedListener {
            //do nothing, don't refresh the fragment
        }
    }

}