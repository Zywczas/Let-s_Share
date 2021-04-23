package com.zywczas.letsshare.fragments.groups.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentGroupsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.fragments.groups.adapter.GroupsAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.hideSoftKeyboard
import com.zywczas.letsshare.utils.showSnackbar
import com.zywczas.letsshare.utils.turnOffOnBackPressed
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupsFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: GroupsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupsBinding by autoRelease()
    private val groupsAdapter by lazy { GroupsAdapter{ group ->
        lifecycleScope.launchWhenResumed {
            viewModel.saveCurrentlyOpenGroupId(group.id)
            findNavController().navigate(GroupsFragmentDirections.toGroupDetailsFragment(group))
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
        turnOffOnBackPressed()
        lifecycleScope.launch { viewModel.getGroups() }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapter = groupsAdapter
        }
        binding.bottomNavBar.selectedItemId = R.id.groupsFragment
        setupObservers()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it)}
        viewModel.groups.observe(viewLifecycleOwner){ groupsAdapter.submitList(it.toMutableList()) }
    }

    private fun setupOnClickListeners(){
        binding.addGroup.setOnClickListener { lifecycleScope.launchWhenResumed {
            hideSoftKeyboard()
            viewModel.addGroup(binding.groupName.text.toString(), "zł") //todo pozniej dac rozne waluty
        }}
        binding.bottomNavBar.setOnNavigationItemSelectedListener(bottomNavClick)
    }

    private val bottomNavClick = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId){
            R.id.friendsFragment -> {
                findNavController().navigate(GroupsFragmentDirections.toFriendsFragment())
                true
            }
            R.id.settingsFragment -> {
                findNavController().navigate(GroupsFragmentDirections.toSettingsFragment())
                true
            }
            else -> false
        }
    }

}