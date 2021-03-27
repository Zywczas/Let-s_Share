package com.zywczas.letsshare.fragments.groupdetails.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.model.GroupMemberDomain

class GroupMembersAdapter(private val currency: String) : ListAdapter<GroupMemberDomain, GroupMembersAdapter.ViewHolder>(object : DiffUtil.ItemCallback<GroupMemberDomain>() {

    override fun areItemsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean = oldItem.email == newItem.email

    override fun areContentsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean =
        oldItem.email == newItem.email &&
            oldItem.percentage_share == newItem.percentage_share &&
            oldItem.expenses == newItem.expenses
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.memberName)
        private val expenses: TextView = itemView.findViewById(R.id.memberExpenses)
        private val share: TextView = itemView.findViewById(R.id.percentageShare)

        @SuppressLint("SetTextI18n")
        fun bindMember(member: GroupMemberDomain) {
            name.text = member.name
            expenses.text = "${member.expenses} $currency"
            share.text = "${member.percentage_share} %"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_members_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindMember(getItem(position))

}