package com.zywczas.letsshare.fragmentlogin.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zywczas.letsshare.databinding.FragmentLoginBinding
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class LoginFragment @Inject constructor(
    private val viewModel : LoginViewModel
) : Fragment(){

    private var binding: FragmentLoginBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
    }

    private fun setupOnClickListeners(){
        binding.register.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionToRegisterFragment())
        }
    }

}