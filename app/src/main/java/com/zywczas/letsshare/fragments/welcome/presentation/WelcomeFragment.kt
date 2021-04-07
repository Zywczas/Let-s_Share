package com.zywczas.letsshare.fragments.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zywczas.letsshare.databinding.FragmentWelcomeBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class WelcomeFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: WelcomeViewModel by viewModels { viewModelFactory }
    private var binding: FragmentWelcomeBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        setupObservers()
    }

    private fun setupObservers(){
        viewModel.goToFriendsFragment.observe(viewLifecycleOwner){
            if (it) { findNavController().navigate(WelcomeFragmentDirections.toFriendsFragment()) }
        }
        viewModel.goToLoginFragment.observe(viewLifecycleOwner){
            if (it) { findNavController().navigate(WelcomeFragmentDirections.toLoginFragment()) }
        }
    }

}