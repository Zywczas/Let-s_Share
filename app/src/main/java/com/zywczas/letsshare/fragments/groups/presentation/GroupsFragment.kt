package com.zywczas.letsshare.fragments.groups.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.zywczas.letsshare.R
import com.zywczas.letsshare.adapters.GroupsAdapter
import com.zywczas.letsshare.databinding.FragmentGroupsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.extentions.dimBackgroundOnMainButtonClick
import com.zywczas.letsshare.extentions.showSnackbar
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class GroupsFragment @Inject constructor(viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: GroupsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupsBinding by autoRelease()
    private val groupsAdapter by lazy { GroupsAdapter{ group ->
            viewModel.saveCurrentlyOpenGroupId(group.id)
            findNavController().navigate(GroupsFragmentDirections.toGroupDetailsFragment(group))
    }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserGroups()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapter = groupsAdapter
        }
        binding.bottomNavBar.selectedItemId = R.id.groupsFragment
        setupObservers()
        setupSpeedDial()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it)}
        viewModel.groups.observe(viewLifecycleOwner){ groupsAdapter.submitList(it.toMutableList()) }
    }

    private fun setupSpeedDial(){
        setupSpeedDialMenu()
        setupSpeedDialMenuClick()
        binding.speedDial.dimBackgroundOnMainButtonClick(requireActivity(), binding.mainLayout)
    }

    private fun setupSpeedDialMenu(){
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.addGroup, R.drawable.ic_add_friend)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.firstFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel(getString(R.string.add_group))
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
    }

    private fun setupSpeedDialMenuClick(){
        binding.speedDial.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.addGroup -> {
                    binding.speedDial.close()
                    showAddGroupDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showAddGroupDialog(){
        AddGroupDialog().show(childFragmentManager, "AddGroupDialog")
    }

    private fun setupOnClickListeners(){
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