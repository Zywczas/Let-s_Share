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
import com.zywczas.letsshare.utils.GROUP_ID_KEY
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showToast
import kotlinx.coroutines.launch
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
        lifecycleScope.launch { viewModel.getMembers(args.group.id) }
        lifecycleScope.launch { viewModel.getExpenses(args.group.id) }
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
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.groupSettingsFABColor))
                .setFabImageTintColor(Color.WHITE) //todo sprobowac wrzucic to w layout, moze w item
                .setLabel("Ustawienia grupy")
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.addExpense, R.drawable.ic_add_expense)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.addExpenseFABColor))
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
//            AppCompatResources.getColorStateList(requireContext(), R.color.purple_700) todo inny sposob na pobranie zasobow
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.myColorPrimaryVariantAlpha03)
            binding.mainLayout.alpha = 0.3F
        } else {
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.myColorPrimaryVariant)
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
        findNavController().navigate(GroupDetailsFragmentDirections.toGroupSettingsFragment(args.group.currency))
    }

    private fun showAddGroupMemberDialog(){
        //todo tu jescze mozna sprobowac wstrzykiwac daggerem fabryke w konstruktor i
        //todo dc odpowiedni scope dla tego view modelu :)
    //todo sprawdzic co sie stanie jak dam fabryke jako singleton, czy bedzie mi wstzykiwac wszedzie te same view modele
//        findNavController().navigate(GroupDetailsFragmentDirections.actionShowAddFriendToGroupDialog(args.group.id)) //todo sprobowc to zrobic albo usunac z nav graph, albo zauktualizowac nazwy w nav graph
        val dialog = AddGroupMemberDialog()
        dialog.arguments = Bundle().apply { putString(GROUP_ID_KEY, args.group.id) }
        dialog.show(childFragmentManager, "AddGroupMemberDialog")
    }

    private fun showAddExpenseDialog(){
        //todo tu jescze mozna sprobowac wstrzykiwac daggerem fabryke w konstruktor i
        val dialog = AddExpenseDialog()
        dialog.arguments = Bundle().apply { putString(GROUP_ID_KEY, args.group.id) }
        dialog.show(childFragmentManager, "AddExpenseDialog")
    }

}