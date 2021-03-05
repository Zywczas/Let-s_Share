package com.zywczas.letsshare.fragmentregister.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentRegisterBinding
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showToast
import javax.inject.Inject

class RegisterFragment @Inject constructor(
    private val viewModel : RegisterViewModel
) : Fragment(){

    private lateinit var binding: FragmentRegisterBinding
//    by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupOnClickListeners()
    }

//todo dodac hide/show password
    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
        viewModel.isUserRegisteredAndUserName.observe(viewLifecycleOwner) { isRegisteredAndName ->
            if (isRegisteredAndName.first){
                val message = getString(R.string.user_registered, isRegisteredAndName.second)
                showToast(message)
                goBackToLoginFragment()
            }
        }
    }

    private fun goBackToLoginFragment() = requireActivity().onBackPressed() //todo sprawdzic czy klawiatura dobrze sie zamyka i czy wraca do loginu

    private fun setupOnClickListeners(){
        binding.register.setOnClickListener{
            hideSoftKeyboard()
            registerNewUser()
        }
    }

    private fun hideSoftKeyboard() {
        val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        }
    }

    private fun registerNewUser(){
        lifecycleScope.launchWhenResumed {
            viewModel.registerUser(
                binding.name.text.toString(),
                binding.email.text.toString(),
                binding.password.text.toString()
            )
        }
    }

}