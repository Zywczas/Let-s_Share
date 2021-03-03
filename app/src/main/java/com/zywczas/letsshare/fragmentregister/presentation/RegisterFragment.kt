package com.zywczas.letsshare.fragmentregister.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.zywczas.letsshare.databinding.FragmentRegisterBinding
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showToast
import javax.inject.Inject

class RegisterFragment @Inject constructor(
    private val viewModel : RegisterViewModel
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
        setupObservers()
        setupOnClickListeners()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
        viewModel.isRegistered.observe(viewLifecycleOwner){ //todo
        }
    }

    private fun setupOnClickListeners(){
        binding.register.setOnClickListener{
            lifecycleScope.launchWhenResumed {
                viewModel.verifyCredentialsAndRegisterNewUser(binding.email.text.toString(), binding.password.text.toString())
            }
        }
    }

}