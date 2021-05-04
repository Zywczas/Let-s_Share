package com.zywczas.letsshare.fragments.groupsettings.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.zywczas.letsshare.R
import com.zywczas.letsshare.adapters.GroupMembersSettingsAdapter
import com.zywczas.letsshare.databinding.FragmentGroupSettingsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.di.modules.UtilsModule.TextListenerDebounce
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.dimBackgroundOnMainButtonClick
import com.zywczas.letsshare.utils.showSnackbar
import javax.inject.Inject

class GroupSettingsFragment @Inject constructor(
    viewModelFactory: UniversalViewModelFactory,
    @TextListenerDebounce private val textDebounce: Long
) : Fragment() {

    private val viewModel: GroupSettingsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupSettingsBinding by autoRelease()
    private val args: GroupSettingsFragmentArgs by navArgs()
    private val membersAdapter by lazy { GroupMembersSettingsAdapter(lifecycle, textDebounce){
        memberId, split -> viewModel.updatePercentage(memberId, split)
    } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMonthSettings(args.month)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            membersAdapterXML = membersAdapter
        }
        binding.toolbar.setupWithNavController(findNavController())
        setupObservers()
        setupSpeedDial()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.members.observe(viewLifecycleOwner){ membersAdapter.submitList(it.toMutableList()) }
        viewModel.totalPercentage.observe(viewLifecycleOwner){ binding.splitTotalValue.text = it }
        viewModel.areSettingsChanged.observe(viewLifecycleOwner){ binding.save.isVisible = it }
    }

    private fun setupSpeedDial(){
        setupSpeedDialMenu()
        setupSpeedDialMenuClick()
        binding.speedDial.dimBackgroundOnMainButtonClick(requireActivity(), binding.mainLayout)
    }

    private fun setupSpeedDialMenu(){
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.removeMember, R.drawable.ic_remove_member)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel(getString(R.string.remove_member))
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.addMember, R.drawable.ic_add_friend)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.firstFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel(getString(R.string.add_new_member))
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
    }

    private fun setupSpeedDialMenuClick(){
        binding.speedDial.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.addMember -> {
                    binding.speedDial.close()
                    showAddGroupMemberDialog()
                    true
                }
                R.id.removeMember -> {
                    binding.speedDial.close()
                    showRemoveMemberDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showAddGroupMemberDialog(){
        AddGroupMemberDialog().show(childFragmentManager, "AddGroupMemberDialog")
    }

    private fun showRemoveMemberDialog(){
        RemoveMemberDialog().show(childFragmentManager, "RemoveMemberDialog")
    }

    private fun setupOnClickListeners(){
        binding.equalSplit.setOnClickListener { viewModel.setEqualSplits() }
        binding.save.setOnClickListener { viewModel.saveSplits() }
    }

}