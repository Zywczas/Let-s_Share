
package com.zywczas.letsshare.fragments.friends.presentation

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentFriendsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.fragments.friends.adapter.FriendsAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showSnackbar
import com.zywczas.letsshare.utils.turnOffOnBackPressed
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: FriendsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentFriendsBinding by autoRelease()
    private val adapter by lazy { FriendsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        turnOffOnBackPressed()
        viewModel.getFriends()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapterXML = adapter
        }
        binding.bottomNavBar.selectedItemId = R.id.friendsFragment
        setupObservers()
        setupSpeedDialMenu()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.friends.observe(viewLifecycleOwner){ adapter.submitList(it.toMutableList()) }
    }

    private fun setupSpeedDialMenu(){
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.addFriend, R.drawable.ic_add_friend)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.firstFABItem))
                .setFabImageTintColor(Color.WHITE)
                .setLabel(getString(R.string.add_friend))
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
            R.id.groupsFragment -> {
                findNavController().navigate(FriendsFragmentDirections.toGroupsFragment())
                true
            }
            R.id.settingsFragment -> {
                findNavController().navigate(FriendsFragmentDirections.toSettingsFragment())
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
                R.id.addFriend -> {
                    binding.speedDial.close()
                    showAddFriendDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showAddFriendDialog(){
        AddFriendDialog().show(childFragmentManager,"AddFriendDialog")
    }

}