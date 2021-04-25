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
import com.leinardi.android.speeddial.SpeedDialView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentGroupsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.fragments.groups.adapter.GroupsAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showSnackbar
import com.zywczas.letsshare.utils.turnOffOnBackPressed
import javax.inject.Inject

class GroupsFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: GroupsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupsBinding by autoRelease()
    private val groupsAdapter by lazy { GroupsAdapter{ group ->
            viewModel.saveCurrentlyOpenGroupId(group.id)
            findNavController().navigate(GroupsFragmentDirections.toGroupDetailsFragment(group))
    }}

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
        viewModel.getUserGroups()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapter = groupsAdapter
        }
        binding.bottomNavBar.selectedItemId = R.id.groupsFragment
        setupObservers()
        setupSpeedDialMenu()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it)}
        viewModel.groups.observe(viewLifecycleOwner){ groupsAdapter.submitList(it.toMutableList()) }
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

    private fun setupOnClickListeners(){
        binding.bottomNavBar.setOnNavigationItemSelectedListener(bottomNavClick)
        setupSpeedDialMainBtnClick()
        setupSpeedDialMenuClick()
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

    private fun setupSpeedDialMainBtnClick(){
        binding.speedDial.setOnChangeListener(object : SpeedDialView.OnChangeListener{
            override fun onMainActionSelected(): Boolean {
                return false
            }
            override fun onToggleChanged(isOpen: Boolean) {
                dimOrRestoreBackground(isOpen)
            }
        })
    }

    private fun dimOrRestoreBackground(isDialOpen : Boolean){
        val window = requireActivity().window
        if (isDialOpen){
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primaryVariantAlpha03)
            binding.mainLayout.alpha = 0.3F
        } else {
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primaryVariant)
            binding.mainLayout.alpha = 1F
        }
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

}