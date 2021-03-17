package com.zywczas.letsshare.fragmentgroups.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentGroupsBinding
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.getCurrentDateString
import com.zywczas.letsshare.utils.getMonthId
import java.util.*
import javax.inject.Inject

class GroupsFragment @Inject constructor(private val viewModel: GroupsViewModel) : Fragment() {

    private var binding: FragmentGroupsBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        setupBottomNavBar()
        binding.data.text = Date().getCurrentDateString()
    }

    private fun setupBottomNavBar(){
        binding.bottomNavBar.setupWithNavController(findNavController())
        binding.bottomNavBar.setOnNavigationItemReselectedListener {
            //do nothing, don't refresh the fragment
        }
    }

}