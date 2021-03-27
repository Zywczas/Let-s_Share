package com.zywczas.letsshare.fragments.groupdetails.presentation

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
    }

    private fun setupSpeedDialMenu(){ //todo dokonczyc, kolory, ikonki, text ze stringow
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.addFriendToGroup, R.drawable.ic_launcher_foreground)
                .setLabel("Dodaj znajomego")
                .setLabelClickable(true)
                .create()
        )
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.addExpense, R.drawable.ic_launcher_foreground)
                .setLabel("Dodaj wydatek")
                .setLabelClickable(true)
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
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.purple_700_alpha03)
            binding.mainLayout.alpha = 0.3F
        } else {
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.purple_700) //todo poprawic te kolory, zeby bralo ten z theme
            binding.mainLayout.alpha = 1F
        }
    }

    private fun setupSpeedDialMenuClick(){
        binding.speedDial.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.addFriendToGroup -> {
                    binding.speedDial.close()
                    showAddFriendToGroupDialog()
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
//todo zmienic nazwe na mamber i layoutu tez
    private fun showAddFriendToGroupDialog(){
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