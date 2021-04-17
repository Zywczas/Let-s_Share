package com.zywczas.letsshare.fragments.groupdetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.model.GroupMemberDomain
import java.util.*

class GroupMembersAdapter(private val currency: String) : ListAdapter<GroupMemberDomain, GroupMembersAdapter.ViewHolder>(object : DiffUtil.ItemCallback<GroupMemberDomain>() {

    override fun areItemsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean = oldItem.email == newItem.email

    override fun areContentsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean =
        oldItem.email == newItem.email &&
            oldItem.percentageShare == newItem.percentageShare &&
            oldItem.expenses == newItem.expenses &&
            oldItem.owesOrOver == newItem.owesOrOver &&
            oldItem.balance == newItem.balance
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.memberName)
        private val expenses: TextView = itemView.findViewById(R.id.memberExpenses)
        private val share: TextView = itemView.findViewById(R.id.percentageShare)
        private val owes: TextView = itemView.findViewById(R.id.owes)
        private val difference: TextView = itemView.findViewById(R.id.difference)

        fun bindMember(member: GroupMemberDomain) {
            name.text = member.name
            expenses.text = String.format(Locale.UK, "%.2f %s", member.expenses, currency)
            share.text = String.format(Locale.UK, "%.2f %s", member.percentageShare, "%")
            owes.text = member.owesOrOver
            difference.text = String.format(Locale.UK, "%.2f %s", member.balance, "%")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_group_members, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindMember(getItem(position))

}