package com.zywczas.letsshare.fragmentgroups.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentGroupsBinding
import com.zywczas.letsshare.fragmentfriends.adapter.FriendsAdapter
import com.zywczas.letsshare.fragmentgroups.adapter.GroupsAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.hideSoftKeyboard
import com.zywczas.letsshare.utils.showToast
import com.zywczas.letsshare.utils.today
import java.util.*
import javax.inject.Inject

class GroupsFragment @Inject constructor(private val viewModel: GroupsViewModel) : Fragment() {

    private var binding: FragmentGroupsBinding by autoRelease()
    private val adapter by lazy { GroupsAdapter{
        findNavController().navigate(GroupsFragmentDirections.actionGroupsToGroupDetailsFragment(it))
    } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        binding.toolbar.setTitle(R.string.groups)
        setupRecycler()
        setupObservers()
        setupOnClickListeners()
        setupBottomNavBar()
    }

    private fun setupRecycler(){
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.setHasFixedSize(true)
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it)}
        viewModel.groups.observe(viewLifecycleOwner){ adapter.submitList(it.toMutableList()) }
    }

    private fun setupOnClickListeners(){
        binding.addGroup.setOnClickListener { lifecycleScope.launchWhenResumed {
            hideSoftKeyboard()
            viewModel.addGroup(binding.groupName.text.toString(), "zł") //todo pozniej dac rozne waluty
        }}
    }

    private fun setupBottomNavBar(){
        binding.bottomNavBar.setupWithNavController(findNavController())
        binding.bottomNavBar.setOnNavigationItemReselectedListener {
            //do nothing, don't refresh the fragment
        }
    }

}