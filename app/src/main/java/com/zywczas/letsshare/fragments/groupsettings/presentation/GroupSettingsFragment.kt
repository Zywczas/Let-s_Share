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
import com.leinardi.android.speeddial.SpeedDialView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.adapters.GroupMembersSettingsAdapter
import com.zywczas.letsshare.databinding.FragmentGroupSettingsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.di.modules.UtilsModule.TextListenerDebounce
import com.zywczas.letsshare.utils.autoRelease
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
        setupToolbar()
        setupObservers()
        setupSpeedDialMenu()
        setupOnClickListeners()
    }

    private fun setupToolbar(){
        binding.toolbar.setupWithNavController(findNavController())
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.members.observe(viewLifecycleOwner){ membersAdapter.submitList(it.toMutableList()) }
        viewModel.totalPercentage.observe(viewLifecycleOwner){ binding.splitTotalValue.text = it }
        viewModel.isPercentageChanged.observe(viewLifecycleOwner){ binding.save.isVisible = it }
    }

    private fun setupSpeedDialMenu(){
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

    private fun setupOnClickListeners(){
        setupSpeedDialMainBtnClick()
        setupSpeedDialMenuClick()
        binding.equalSplit.setOnClickListener { viewModel.setEqualSplits() }
        binding.save.setOnClickListener { viewModel.saveSplits() }
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
                R.id.addMember -> {
                    binding.speedDial.close()
                    showAddGroupMemberDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showAddGroupMemberDialog(){
        AddGroupMemberDialog().show(childFragmentManager, "AddGroupMemberDialog")
    }

}