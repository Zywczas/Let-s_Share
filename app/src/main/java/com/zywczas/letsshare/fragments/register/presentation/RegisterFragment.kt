package com.zywczas.letsshare.fragments.register.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentRegisterBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.extentions.hideSoftKeyboard
import com.zywczas.letsshare.extentions.showSnackbar
import com.zywczas.letsshare.utils.autoRelease
import javax.inject.Inject

class RegisterFragment @Inject constructor(viewModelFactory: UniversalViewModelFactory) : Fragment(){

    private val viewModel: RegisterViewModel by viewModels { viewModelFactory }
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
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        binding.toolbar.setupWithNavController(findNavController())
        setupObservers()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it) }
        viewModel.isRegisteredAndUserName.observe(viewLifecycleOwner) { isRegisteredAndName ->
            if (isRegisteredAndName.first){
                val dialogTitle = getString(R.string.hello_user, isRegisteredAndName.second)
                showInfoAlertDialog(dialogTitle)
                goToLoginFragment()
            }
        }
    }

    private fun showInfoAlertDialog(title: String){
        findNavController().navigate(RegisterFragmentDirections.showInfoDialog(title))
    }

    private fun goToLoginFragment() = requireActivity().onBackPressed()

    private fun setupOnClickListeners(){
        binding.register.setOnClickListener{
            hideSoftKeyboard()
            registerUser()
        }
    }

    private fun registerUser(){
        viewModel.registerUser(
            binding.name.text.toString(),
            binding.email1.text.toString(),
            binding.email2.text.toString(),
            binding.password1.text.toString(),
            binding.password2.text.toString()
        )
    }

}