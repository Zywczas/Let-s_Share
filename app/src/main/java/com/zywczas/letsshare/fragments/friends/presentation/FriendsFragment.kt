
package com.zywczas.letsshare.fragments.friends.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentFriendsBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.fragments.friends.adapter.FriendsAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.showToast
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
        lifecycleScope.launch { viewModel.getFriends() }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapterXML = adapter
        }
        binding.toolbar.setTitle(R.string.friends)
        binding.bottomNavBar.selectedItemId = R.id.friendsFragment
        setupObservers()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
        viewModel.friends.observe(viewLifecycleOwner){ adapter.submitList(it.toMutableList()) }
    }

    private fun setupOnClickListeners(){
        binding.addFriendBtn.setOnClickListener { addFriendOnClickListener() }
        binding.bottomNavBar.setOnNavigationItemSelectedListener(bottomNavClick)
    }

    private fun addFriendOnClickListener(){
        binding.addFriendLayout.isVisible = true
        binding.addFriendByEmail.setOnClickListener {
            lifecycleScope.launchWhenResumed { viewModel.addFriend(binding.friendEmail.text.toString()) }
        }
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