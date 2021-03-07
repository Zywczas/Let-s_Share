package com.zywczas.letsshare.fragmenthome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zywczas.letsshare.databinding.FragmentHomeBinding
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class HomeFragment @Inject constructor(private val viewModel: HomeViewModel) : Fragment() {

    private var binding: FragmentHomeBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        setupObservers()
    }

    private fun setupObservers(){
        viewModel.goToMainFragment.observe(viewLifecycleOwner){
            if (it) { findNavController().navigate(HomeFragmentDirections.actionHomeToMainFragment()) }
        }
        viewModel.goToLoginFragment.observe(viewLifecycleOwner){
            if (it) { findNavController().navigate(HomeFragmentDirections.actionHomeToLoginFragment()) }
        }
    }

}