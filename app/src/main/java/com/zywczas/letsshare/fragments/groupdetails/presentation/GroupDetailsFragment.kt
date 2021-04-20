package com.zywczas.letsshare.fragments.groupdetails.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentGroupDetailsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.fragments.groupdetails.adapters.ExpensesAdapter
import com.zywczas.letsshare.fragments.groupdetails.adapters.GroupMembersAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.monthId
import com.zywczas.letsshare.utils.showToast
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class GroupDetailsFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory): Fragment() {

    private val viewModel: GroupDetailsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupDetailsBinding by autoRelease()
    private val args: GroupDetailsFragmentArgs by navArgs()
    private val membersAdapter by lazy { GroupMembersAdapter(args.group.currency) }
    private val expensesAdapter by lazy { ExpensesAdapter(args.group.currency) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch { viewModel.getMonthDetails() }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            membersAdapterXML = membersAdapter
            expensesAdapterXML = expensesAdapter
        }
        setupToolbar()
        setupObservers()
        setupSpeedDialMenu()
        setupOnClickListeners()
    }

    private fun setupToolbar(){
        binding.toolbar.title = args.group.name
        binding.toolbar.setupWithNavController(findNavController())
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
        viewModel.members.observe(viewLifecycleOwner){ membersAdapter.submitList(it.toMutableList()) }
        viewModel.expenses.observe(viewLifecycleOwner){ expensesAdapter.submitList(it.toMutableList()) }
    }

    private fun setupSpeedDialMenu(){ //todo dokonczyc text ze stringow
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.groupSettings, R.drawable.ic_settings_24)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondFABItem))
                .setFabImageTintColor(Color.WHITE) //todo sprobowac wrzucic to w layout, moze w item
                .setLabel("Ustawienia grupy")
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.addExpense, R.drawable.ic_add_expense)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.firstFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel("Dodaj wydatek")
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
    }

    private fun setupOnClickListeners(){
        setupSpeedDialMainBtnClick()
        setupSpeedDialMenuClick()
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
                else -> false
            }
        }
    }

    private fun goToGroupSettingFragment(){
        findNavController().navigate(GroupDetailsFragmentDirections.toGroupSettingsFragment(Date().monthId()))
    }

    private fun showAddExpenseDialog(){
        AddExpenseDialog().show(childFragmentManager, "AddExpenseDialog")
    }

}