package com.zywczas.letsshare.fragments.historydetails.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.zywczas.letsshare.R
import com.zywczas.letsshare.adapters.ExpenseItem
import com.zywczas.letsshare.adapters.GroupMemberItemDetails
import com.zywczas.letsshare.databinding.FragmentHistoryDetailsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.extentions.addTransparentItemDivider
import com.zywczas.letsshare.extentions.showSnackbar
import com.zywczas.letsshare.fragments.groupdetails.presentation.GroupDetailsFragmentDirections
import com.zywczas.letsshare.models.ExpenseDomain
import com.zywczas.letsshare.models.GroupMemberDomain
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class HistoryDetailsFragment @Inject constructor(viewModelFactory: UniversalViewModelFactory) : Fragment() {

    val viewModel: HistoryDetailsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentHistoryDetailsBinding by autoRelease()
    private val args: HistoryDetailsFragmentArgs by navArgs()
    private val membersItemAdapter by lazy { ItemAdapter<GroupMemberItemDetails>() }
    private val expensesItemAdapter by lazy { ItemAdapter<ExpenseItem>() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMonthDetails(args.groupMonth)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            membersAdapterXML = FastAdapter.with(membersItemAdapter)
            expensesAdapterXML = FastAdapter.with(expensesItemAdapter)
        }
        binding.toolbar.setupWithNavController(findNavController())
        binding.membersRecycler.addTransparentItemDivider()
        setupObservers()
        setupSpeedDial()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.monthlySum.observe(viewLifecycleOwner){ binding.toolbar.title = "${args.group.name} - $it ${args.group.currency}" }
        viewModel.members.observe(viewLifecycleOwner){ FastAdapterDiffUtil.set(membersItemAdapter, it.toMemberItems(), GroupMemberItemDetails.DiffUtil()) }
        viewModel.expenses.observe(viewLifecycleOwner){ FastAdapterDiffUtil.set(expensesItemAdapter, it.toExpenseItems(), ExpenseItem.DiffUtil()) }
        viewModel.settledUpMessage.observe(viewLifecycleOwner){ binding.settledUpMessage.text = getString(it) }
    }

    private fun List<GroupMemberDomain>.toMemberItems() = map {
        GroupMemberItemDetails(it, args.group.currency)
    }

    private fun List<ExpenseDomain>.toExpenseItems() = map {
        ExpenseItem(it, args.group.currency)
    }

    private fun setupSpeedDial(){
        setupSpeedDialMenu()
        setupSpeedDialMenuClick()
    }

    private fun setupSpeedDialMenu(){
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.groupSettings, R.drawable.ic_settings)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel(getString(R.string.group_settings))
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.settleUp, R.drawable.ic_dolar)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.firstFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel(getString(R.string.settle_up))
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
    }

    private fun setupSpeedDialMenuClick(){
        binding.speedDial.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.groupSettings -> {
                    binding.speedDial.close()
                    goToGroupSettingFragment()
                    true
                }
                R.id.settleUp -> {
                    binding.speedDial.close()
                    showSettleUpDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun goToGroupSettingFragment(){
        findNavController().navigate(GroupDetailsFragmentDirections.toGroupSettingsFragment(args.groupMonth))
    }

    private fun showSettleUpDialog(){
        SettleUpDialog().show(childFragmentManager, "AddExpenseDialog")
    }

}