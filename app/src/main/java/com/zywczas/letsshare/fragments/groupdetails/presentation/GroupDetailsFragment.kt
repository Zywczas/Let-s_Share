package com.zywczas.letsshare.fragments.groupdetails.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.drag.ItemTouchCallback
import com.mikepenz.fastadapter.swipe.SimpleSwipeCallback
import com.mikepenz.fastadapter.swipe_drag.SimpleSwipeDragCallback
import com.zywczas.letsshare.R
import com.zywczas.letsshare.adapters.ExpenseItem
import com.zywczas.letsshare.adapters.GroupMemberItemDetails
import com.zywczas.letsshare.databinding.FragmentGroupDetailsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.models.ExpenseDomain
import com.zywczas.letsshare.models.GroupMemberDomain
import com.zywczas.letsshare.utils.addTransparentItemDivider
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.dimBackgroundOnMainButtonClick
import com.zywczas.letsshare.utils.showSnackbar
import javax.inject.Inject

class GroupDetailsFragment @Inject constructor(viewModelFactory: UniversalViewModelFactory) :
    Fragment(), ItemTouchCallback, SimpleSwipeCallback.ItemSwipeCallback {

    private val viewModel: GroupDetailsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupDetailsBinding by autoRelease()
    private val args: GroupDetailsFragmentArgs by navArgs()
    private val membersItemAdapter by lazy { ItemAdapter<GroupMemberItemDetails>() }
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
            membersAdapterXML = FastAdapter.with(membersItemAdapter)
            expensesAdapterXML = expensesAdapter
        }
        binding.toolbar.setupWithNavController(findNavController())
        binding.membersRecycler.addTransparentItemDivider()
        setupObservers()
        setupSpeedDial()
        setupExpenseAdapter()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.monthlySum.observe(viewLifecycleOwner){ binding.toolbar.title = "${args.group.name} - $it ${args.group.currency}" }
        viewModel.members.observe(viewLifecycleOwner){ FastAdapterDiffUtil.set(membersItemAdapter, it.toMemberItems(), GroupMemberItemDetails.DiffUtil()) }
        viewModel.expenses.observe(viewLifecycleOwner){ FastAdapterDiffUtil.set(expensesItemAdapter, it.toExpenseItems(), ExpenseItem.DiffUtil()) }
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

        expensesAdapter.onClickListener = { _, _, _, _ ->
            showSnackbar(R.string.swipe_to_delete)
            false
        }

        val leaveBehindIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!
        val touchCallback = SimpleSwipeDragCallback(
            this,
            this,
            leaveBehindIcon,
            ItemTouchHelper.LEFT,
            Color.RED
        )
            .withBackgroundSwipeRight(Color.RED)
            .withLeaveBehindSwipeRight(leaveBehindIcon)
            .withSensitivity(10f)
            .withSurfaceThreshold(0.6f)

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(binding.expensesRecycler)
    }

    override fun itemSwiped(position: Int, direction: Int) = viewModel.deleteExpense(position)

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean = false

}