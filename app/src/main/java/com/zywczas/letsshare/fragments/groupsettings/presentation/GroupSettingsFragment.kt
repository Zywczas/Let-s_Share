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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentGroupSettingsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.fragments.groupsettings.adapters.GroupMembersSettingsAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showToast
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupSettingsFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: GroupSettingsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentGroupSettingsBinding by autoRelease()
    private val args: GroupSettingsFragmentArgs by navArgs()
    private val membersAdapter by lazy { GroupMembersSettingsAdapter(lifecycle){
        memberId, split -> lifecycleScope.launchWhenResumed { viewModel.updatePercentage(memberId, split) }
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
        lifecycleScope.launch { viewModel.getMembers(args.monthId) }
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
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
        viewModel.members.observe(viewLifecycleOwner){ membersAdapter.submitList(it.toMutableList()) }
        viewModel.totalPercentage.observe(viewLifecycleOwner){ binding.splitTotalValue.text = it }
        viewModel.isPercentageChanged.observe(viewLifecycleOwner){ binding.save.isVisible = it }
    }

    private fun setupSpeedDialMenu(){ //todo dokonczyc text ze stringow
//        binding.speedDial.addActionItem(
//            SpeedDialActionItem.Builder(R.id.groupSettings, R.drawable.ic_settings_24)
//                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.groupSettingsFABColor))
//                .setFabImageTintColor(Color.WHITE) //todo sprobowac wrzucic to w layout, moze w item
//                .setLabel("Ustawienia grupy")
//                .setLabelClickable(true)
//                .setLabelBackgroundColor(Color.WHITE)
//                .create()
//        )
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.addMember, R.drawable.ic_add_friend)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.firstFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel("Dodaj czlonka grupy")
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
    }

    private fun setupOnClickListeners(){
        setupSpeedDialMainBtnClick()
        setupSpeedDialMenuClick()
        binding.equalSplit.setOnClickListener { lifecycleScope.launchWhenResumed { viewModel.setEqualSplits() } }
        binding.save.setOnClickListener { lifecycleScope.launchWhenResumed { viewModel.saveSplits() } }
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
        //todo tu jescze mozna sprobowac wstrzykiwac daggerem fabryke w konstruktor i
        //todo dc odpowiedni scope dla tego view modelu :)
        //todo sprawdzic co sie stanie jak dam fabryke jako singleton, czy bedzie mi wstzykiwac wszedzie te same view modele
//        findNavController().navigate(GroupDetailsFragmentDirections.actionShowAddFriendToGroupDialog(args.group.id)) //todo sprobowc to zrobic albo usunac z nav graph, albo zauktualizowac nazwy w nav graph
        AddGroupMemberDialog().show(childFragmentManager, "AddGroupMemberDialog")
    }

}