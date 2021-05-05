package com.zywczas.letsshare.fragments.groupsettings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.zywczas.letsshare.adapters.FriendItem
import com.zywczas.letsshare.databinding.DialogGroupMembersBinding
import com.zywczas.letsshare.models.Friend
import com.zywczas.letsshare.models.GroupMemberDomain
import com.zywczas.letsshare.utils.autoRelease

class RemoveMemberDialog : DialogFragment() {

    private val viewModel: GroupSettingsViewModel by viewModels({ requireParentFragment() })
    private var binding: DialogGroupMembersBinding by autoRelease()
    private val friendItemAdapter by lazy { ItemAdapter<FriendItem>() }
    private val friendAdapter by lazy { FastAdapter.with(friendItemAdapter) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogGroupMembersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFriends()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapterXML = friendAdapter
        }
        setupObservers()
        setupFriendsAdapter()
        binding.cancel.setOnClickListener { dismiss() }
    }

    private fun setupObservers() {
        viewModel.members.observe(viewLifecycleOwner) { FastAdapterDiffUtil.set(friendItemAdapter, it.toFriendItems(), FriendItem.DiffUtil()) }
    }

    private fun List<GroupMemberDomain>.toFriendItems() = map { FriendItem(Friend(id = it.id, email = it.email, name = it.name)) }

    private fun setupFriendsAdapter(){
        friendAdapter.onClickListener = { _, _, item, _ ->
            viewModel.deleteMember(item.friend)
            dismiss()
            false
        }
    }

}