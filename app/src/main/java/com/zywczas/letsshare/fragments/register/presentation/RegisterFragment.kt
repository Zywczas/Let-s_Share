package com.zywczas.letsshare.fragments.register.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentRegisterBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.hideSoftKeyboard
import com.zywczas.letsshare.utils.showToast
import javax.inject.Inject

class RegisterFragment @Inject constructor(private val viewModelFactory: UniversalViewModelFactory) : Fragment(){

    private val viewModel: RegisterViewModel by viewModels { viewModelFactory }
    private var binding: FragmentRegisterBinding by autoRelease()

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
        viewModel.isRegisteredAndUserName.observe(viewLifecycleOwner) { isRegisteredAndName ->
            if (isRegisteredAndName.first){
                val message = getString(R.string.user_registered, isRegisteredAndName.second)
                showToast(message) //todo tu powinien byc alert dialog informujacy o wyslaniu maila i sprawdzic czy moze byc dialog i jednoczesnie zmienic fragment
                goBackToLoginFragment()
            }
        }
    }

    private fun goBackToLoginFragment() = requireActivity().onBackPressed() //todo sprawdzic czy klawiatura dobrze sie zamyka na moim stary telefonie i czy wraca do loginu

    private fun setupOnClickListeners(){
        binding.register.setOnClickListener{
            hideSoftKeyboard()
            registerUser()
        }
    }

    private fun registerUser(){
        lifecycleScope.launchWhenResumed {
            viewModel.registerUser(
                binding.name.text.toString(),
                binding.email.text.toString(),
                binding.password.text.toString()
            )
        }
    }

}