package com.zywczas.letsshare.fragments.groupdetails.presentation

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
import androidx.recyclerview.widget.RecyclerView
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.zywczas.letsshare.R
import com.zywczas.letsshare.adapters.ExpenseItem
import com.zywczas.letsshare.adapters.GroupMembersAdapter
import com.zywczas.letsshare.databinding.FragmentGroupDetailsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.models.ExpenseDomain
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.dimBackgroundOnMainButtonClick
import com.zywczas.letsshare.utils.showSnackbar
import javax.inject.Inject

class GroupDetailsFragment @Inject constructor(viewModelFactory: UniversalViewModelFactory): Fragment() {

    private val viewModel: GroupDetailsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupDetailsBinding by autoRelease()
    private val args: GroupDetailsFragmentArgs by navArgs()
    private val membersAdapter by lazy { GroupMembersAdapter(args.group.currency) }
    private val expensesItemAdapter by lazy { ItemAdapter<ExpenseItem>() }
    private val expensesAdapter by lazy { FastAdapter.with(expensesItemAdapter) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMonthDetails()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            membersAdapterXML = membersAdapter
            expensesAdapterXML = expensesAdapter
        }
        binding.toolbar.setupWithNavController(findNavController())
        setupObservers()
        setupSpeedDial()
        setupExpenseAdapter()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.monthlySum.observe(viewLifecycleOwner){ binding.toolbar.title = "${args.group.name} - $it ${args.group.currency}" }
        viewModel.members.observe(viewLifecycleOwner){ membersAdapter.submitList(it.toMutableList()) }
        viewModel.expenses.observe(viewLifecycleOwner){ FastAdapterDiffUtil.set(expensesItemAdapter, it.toAdapterItems(), ExpenseItem.DiffUtil()) } //todo jak tu nie ma detect moves to chyba tez powinno przewijac na gore
    }

    private fun List<ExpenseDomain>.toAdapterItems() = map {
        ExpenseItem(it, args.group.currency)
    }

    private fun setupSpeedDial(){
        setupSpeedDialMenu()
        setupSpeedDialMenuClick()
        binding.speedDial.dimBackgroundOnMainButtonClick(requireActivity(), binding.mainLayout)
    }

    private fun setupSpeedDialMenu(){
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.history, R.drawable.ic_history)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.thirdFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel(getString(R.string.history))
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
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
            SpeedDialActionItem.Builder(R.id.addExpense, R.drawable.ic_add_expense)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.firstFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel(getString(R.string.add_expense))
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
                R.id.addExpense -> {
                    binding.speedDial.close()
                    showAddExpenseDialog()
                    true
                }
                R.id.history -> {
                    binding.speedDial.close()
                    goToHistoryFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun goToGroupSettingFragment(){
        viewModel.currentMonth.observe(viewLifecycleOwner){
            findNavController().navigate(GroupDetailsFragmentDirections.toGroupSettingsFragment(it))
        }
    }

    private fun showAddExpenseDialog(){
        AddExpenseDialog().show(childFragmentManager, "AddExpenseDialog")
    }

    private fun goToHistoryFragment(){
        findNavController().navigate(GroupDetailsFragmentDirections.toHistoryFragment(args.group))
    }

    private fun setupExpenseAdapter(){
        expensesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.expensesRecycler.smoothScrollToPosition(0)
            }
        })
    }

}