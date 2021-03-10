
package com.zywczas.letsshare.fragmentmain.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentMainBinding
import com.zywczas.letsshare.fragmentmain.FriendsAdapter
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showToast
import javax.inject.Inject

class MainFragment @Inject constructor(private val viewModel: MainViewModel) : Fragment() {

    private var binding: FragmentMainBinding by autoRelease()
    private val adapter by lazy { FriendsAdapter() }
    private val layoutManager by lazy { LinearLayoutManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        setupToolbar()
        setupRecycler()
        setupObservers()
        setupOnClickListeners()
    }

    private fun setupToolbar(){
        val appBarConfig = AppBarConfiguration(setOf(R.id.mainFragment))
        binding.toolbar.setupWithNavController(findNavController(), appBarConfig)
    }

    private fun setupRecycler(){
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = layoutManager
        binding.recycler.setHasFixedSize(true)
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
        viewModel.friends.observe(viewLifecycleOwner){ adapter.submitList(it.toMutableList()) }
    }

    private fun setupOnClickListeners(){
        binding.logout.setOnClickListener { lifecycleScope.launchWhenResumed { viewModel.logout() } }
        binding.addFriendBtn.setOnClickListener { addFriend() }
    }

    private fun addFriend(){
        binding.addFriendLayout.isVisible = true
        binding.addFriendByEmail.setOnClickListener {
            lifecycleScope.launchWhenResumed { viewModel.addFriendByEmail(binding.friendEmail.text.toString()) }
        }
    }

}