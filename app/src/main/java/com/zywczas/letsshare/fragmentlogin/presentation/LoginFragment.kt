package com.zywczas.letsshare.fragmentlogin.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.zywczas.letsshare.R
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
        binding.isLoggedIn.text = "na razie nie zalogowany"
        setupOnClickListeners()
    }

    private fun setupOnClickListeners(){
        binding.register.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.toRegisterFragment())
            viewModel.registerNewUser(binding.email.text.toString(), binding.password.text.toString())
        }
    }

    private fun registerNewUser(){

    }


}