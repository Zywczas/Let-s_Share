
package com.zywczas.letsshare.fragments.friends.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.zywczas.letsshare.R
import com.zywczas.letsshare.adapters.FriendItem
import com.zywczas.letsshare.databinding.FragmentFriendsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.extentions.getColorFromAttr
import com.zywczas.letsshare.extentions.showSnackbar
import com.zywczas.letsshare.models.Friend
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class FriendsFragment @Inject constructor(viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: FriendsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentFriendsBinding by autoRelease()
    private val friendItemAdapter by lazy { ItemAdapter<FriendItem>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFriends()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapterXML = FastAdapter.with(friendItemAdapter)
        }
        binding.bottomNavBar.selectedItemId = R.id.friendsFragment
        setupObservers()
        setupSpeedDial()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.friends.observe(viewLifecycleOwner){ FastAdapterDiffUtil.set(friendItemAdapter, it.toFriendItems(), FriendItem.DiffUtil()) }
    }

    private fun List<Friend>.toFriendItems() = map { FriendItem(it) }

    private fun setupSpeedDial(){
        setupSpeedDialMenu()
        setupSpeedDialMenuClick()
    }

    private fun setupSpeedDialMenu(){
        binding.speedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.addFriend, R.drawable.ic_add_friend)
                .setFabBackgroundColor(requireContext().getColorFromAttr(R.attr.firstFABItemColor))
                .setFabImageTintColor(Color.WHITE)
                .setLabel(getString(R.string.add_friend))
                .setLabelClickable(true)
                .setLabelBackgroundColor(Color.WHITE)
                .create()
        )
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

    private fun setupOnClickListeners(){
        binding.bottomNavBar.setOnNavigationItemSelectedListener(bottomNavClick)
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

}