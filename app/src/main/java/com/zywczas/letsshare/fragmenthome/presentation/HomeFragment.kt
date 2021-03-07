package com.zywczas.letsshare.fragmenthome.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentHomeBinding
import com.zywczas.letsshare.utils.autoRelease

class HomeFragment : Fragment() {

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
        binding.doLoginu.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionToLoginFragment()) }
    }

}