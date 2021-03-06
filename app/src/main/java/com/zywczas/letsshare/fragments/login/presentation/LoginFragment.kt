package com.zywczas.letsshare.fragments.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zywczas.letsshare.databinding.FragmentLoginBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.extentions.hideSoftKeyboard
import com.zywczas.letsshare.extentions.showSnackbar
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class LoginFragment @Inject constructor(viewModelFactory: UniversalViewModelFactory) : Fragment(){

    private val viewModel: LoginViewModel by viewModels { viewModelFactory }
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
        viewModel.getLastUsedEmail()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        setupOnClickListeners()
        setupObservers()
    }

    private fun setupOnClickListeners(){
        binding.register.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.toRegisterFragment())
        }
        binding.login.setOnClickListener {
            hideSoftKeyboard()
            login()
        }
    }

    private fun login(){
        viewModel.login(binding.email.text.toString(), binding.password.text.toString())
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.lastUsedEmail.observe(viewLifecycleOwner){ binding.email.setText(it) }
        viewModel.isLoggedIn.observe(viewLifecycleOwner){
            if (it){ findNavController().navigate(LoginFragmentDirections.toFriendsFragment()) }
        }
    }

}