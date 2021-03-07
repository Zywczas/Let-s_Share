package com.zywczas.letsshare.fragmentwelcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zywczas.letsshare.databinding.FragmentWelcomeBinding
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class WelcomeFragment @Inject constructor(private val viewModel: WelcomeViewModel) : Fragment() {

    private var binding: FragmentWelcomeBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        setupObservers()
    }

    private fun setupObservers(){
        viewModel.goToMainFragment.observe(viewLifecycleOwner){
            if (it) { findNavController().navigate(WelcomeFragmentDirections.actionWelcomeToMainFragment()) }
        }
        viewModel.goToLoginFragment.observe(viewLifecycleOwner){
            if (it) { findNavController().navigate(WelcomeFragmentDirections.actionWelcomeToLoginFragment()) }
        }
    }

}