package com.zywczas.letsshare.fragments.groupdetails.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zywczas.letsshare.databinding.DialogAddFriendToGroupBinding
import com.zywczas.letsshare.fragments.friends.adapter.FriendsAdapter
import com.zywczas.letsshare.utils.GROUP_ID_KEY
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showToast

class AddGroupMemberDialog : DialogFragment() {

    private val viewModel: GroupDetailsViewModel by viewModels({ requireParentFragment() })
    private var binding: DialogAddFriendToGroupBinding by autoRelease()
    private val groupId by lazy { requireArguments().getString(GROUP_ID_KEY)!! } //todo zamienic pozniej na safe args, jak ogarne view model

    private val adapter by lazy { FriendsAdapter{ friend ->
        lifecycleScope.launchWhenCreated {
            viewModel.addNewMember(friend, groupId)
            dismiss()
        }
    } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddFriendToGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner //todo dac w pozostalych fragmentch tak samo
            adapterXML = adapter
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.friends.observe(viewLifecycleOwner) { adapter.submitList(it.toMutableList()) }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed { viewModel.getFriends() }
    }

}