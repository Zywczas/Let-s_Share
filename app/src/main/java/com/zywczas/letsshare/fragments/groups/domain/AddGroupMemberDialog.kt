package com.zywczas.letsshare.fragments.groups.domain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgsLazy
import androidx.navigation.fragment.navArgs
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.DialogAddFriendToGroupBinding
import com.zywczas.letsshare.fragments.friends.adapter.FriendsAdapter
import com.zywczas.letsshare.fragments.groupdetails.presentation.GroupDetailsFragmentArgs
import com.zywczas.letsshare.fragments.groupdetails.presentation.GroupDetailsViewModel
import com.zywczas.letsshare.utils.GROUP_ID_KEY
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showToast

class AddGroupMemberDialog : DialogFragment() {

    private val viewModel: GroupDetailsViewModel by viewModels({ requireParentFragment() })
    private var binding: DialogAddFriendToGroupBinding by autoRelease()
    private val adapter by lazy { FriendsAdapter{ friend ->
        viewModel.postMessage(R.string.group_added)
    } }
    private val groupId by lazy { requireArguments().getString(GROUP_ID_KEY)!! }

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
        showToast("id: ${groupId}")
    }

}