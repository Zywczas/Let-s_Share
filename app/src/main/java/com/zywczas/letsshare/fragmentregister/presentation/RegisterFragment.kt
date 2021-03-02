package com.zywczas.letsshare.fragmentregister.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentLoginBinding
import com.zywczas.letsshare.databinding.FragmentRegisterBinding
import com.zywczas.letsshare.fragmentlogin.presentation.LoginFragmentDirections
import com.zywczas.letsshare.fragmentlogin.presentation.LoginViewModel
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class RegisterFragment @Inject constructor(
    private val viewModel : LoginViewModel
) : Fragment(){

    private var binding: FragmentRegisterBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
    }

    private fun setupOnClickListeners(){
        binding.register.setOnClickListener{
            viewModel.registerNewUser(binding.email.text.toString(), binding.password.text.toString())
        }
    }

}