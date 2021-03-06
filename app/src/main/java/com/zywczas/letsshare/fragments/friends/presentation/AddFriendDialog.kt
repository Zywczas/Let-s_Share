package com.zywczas.letsshare.fragments.friends.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.zywczas.letsshare.databinding.DialogAddFriendBinding
import com.zywczas.letsshare.utils.autoRelease

class AddFriendDialog : DialogFragment(){

    private val viewModel: FriendsViewModel by viewModels({ requireParentFragment() })
    private var binding: DialogAddFriendBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
    }

    private fun setupOnClickListeners(){
        binding.cancel.setOnClickListener { dismiss() }
        binding.confirm.setOnClickListener {
            viewModel.addFriend(binding.email.text.toString())
            dismiss()
        }
    }

}