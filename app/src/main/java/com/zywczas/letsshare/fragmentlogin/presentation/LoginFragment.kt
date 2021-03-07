package com.zywczas.letsshare.fragmentlogin.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zywczas.letsshare.databinding.FragmentLoginBinding
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showToast
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
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupOnClickListeners()
        setupObservers()
    }

    private fun setupOnClickListeners(){
        binding.register.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionLoginToRegisterFragment())
        }
        binding.login.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                viewModel.login(binding.email.text.toString(), binding.password.text.toString())
            }
        }
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
        viewModel.isLoggedIn.observe(viewLifecycleOwner){
            if (it){ findNavController().navigate(LoginFragmentDirections.actionLoginToMainFragment()) }
        }
    }

}