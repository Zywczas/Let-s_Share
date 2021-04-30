package com.zywczas.letsshare.fragments.groupsettings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zywczas.letsshare.databinding.DialogAddGroupMemberBinding
import com.zywczas.letsshare.adapters.FriendsAdapter
import com.zywczas.letsshare.utils.autoRelease
import kotlinx.coroutines.launch

class AddGroupMemberDialog : DialogFragment() {

    private val viewModel: GroupSettingsViewModel by viewModels({ requireParentFragment() })
    private var binding: DialogAddGroupMemberBinding by autoRelease()

    private val adapter by lazy { FriendsAdapter{ friend ->
            viewModel.addNewMember(friend)
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddGroupMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFriends()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapterXML = adapter
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.friends.observe(viewLifecycleOwner) { adapter.submitList(it.toMutableList()) }
    }

}