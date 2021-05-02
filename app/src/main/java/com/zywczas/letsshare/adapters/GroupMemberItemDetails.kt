package com.zywczas.letsshare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.diff.DiffCallback
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.ItemGroupMembersDetailsBinding
import com.zywczas.letsshare.models.GroupMemberDomain
import java.util.*

class GroupMemberItemDetails(private val member: GroupMemberDomain, private val currency: String) : AbstractBindingItem<ItemGroupMembersDetailsBinding>() {

    override val type: Int = R.id.groupMemberItemDetails

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemGroupMembersDetailsBinding =
        ItemGroupMembersDetailsBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ItemGroupMembersDetailsBinding, payloads: List<Any>) {
        binding.memberName.text = member.name
        binding.memberExpenses.text = String.format(Locale.UK, "%.2f %s", member.expenses, currency)
        binding.percentageShare.text = String.format(Locale.UK, "%.2f%s", member.share, "%")
        binding.owesOrOver.text = binding.root.context.getString(member.owesOrOver)
        binding.difference.text = String.format(Locale.UK, "%.2f %s", member.difference, currency)
    }

    class DiffUtil : DiffCallback<GroupMemberItemDetails> {

        override fun areItemsTheSame(oldItem: GroupMemberItemDetails, newItem: GroupMemberItemDetails): Boolean = oldItem.member.id == newItem.member.id

        override fun areContentsTheSame(oldItem: GroupMemberItemDetails, newItem: GroupMemberItemDetails): Boolean = oldItem.member == newItem.member

        override fun getChangePayload(oldItem: GroupMemberItemDetails, oldItemPosition: Int, newItem: GroupMemberItemDetails, newItemPosition: Int): Any = newItem

    }

}