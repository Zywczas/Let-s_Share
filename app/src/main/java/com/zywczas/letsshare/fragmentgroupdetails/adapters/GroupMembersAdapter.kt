package com.zywczas.letsshare.fragmentgroupdetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.model.GroupMember

class GroupMembersAdapter(private val currency: String) : ListAdapter<GroupMember, GroupMembersAdapter.ViewHolder>(object : DiffUtil.ItemCallback<GroupMember>() {

    override fun areItemsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean = oldItem.email == newItem.email

    override fun areContentsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean =
        oldItem.email == newItem.email &&
            oldItem.percentage_share == newItem.percentage_share &&
            oldItem.expenses == newItem.expenses
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.memberName)
        private val expenses: TextView = itemView.findViewById(R.id.memberExpenses)
        private val currencyView: TextView = itemView.findViewById(R.id.memberCurrency)
        private val share: TextView = itemView.findViewById(R.id.percentageShare)

        fun bindMember(member: GroupMember) {
            name.text = member.name
            expenses.text = member.expenses.toString()
            currencyView.text = currency
            share.text = itemView.context.getString(R.string.percentage_share, member.percentage_share)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_members_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindMember(getItem(position))

}