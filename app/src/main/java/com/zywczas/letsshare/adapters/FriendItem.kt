package com.zywczas.letsshare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.diff.DiffCallback
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.ItemFriendsBinding
import com.zywczas.letsshare.models.Friend

class FriendItem(val friend: Friend) : AbstractBindingItem<ItemFriendsBinding>() {

    override val type: Int = R.id.friendItem

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemFriendsBinding =
        ItemFriendsBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ItemFriendsBinding, payloads: List<Any>) {
        binding.name.text = friend.name
        binding.email.text = friend.email
    }

    class DiffUtil : DiffCallback<FriendItem> {

        override fun areItemsTheSame(oldItem: FriendItem, newItem: FriendItem): Boolean = oldItem.friend.id == newItem.friend.id

        override fun areContentsTheSame(oldItem: FriendItem, newItem: FriendItem): Boolean = oldItem.friend.id == newItem.friend.id

        override fun getChangePayload(oldItem: FriendItem, oldItemPosition: Int, newItem: FriendItem, newItemPosition: Int): Any = newItem

    }

}