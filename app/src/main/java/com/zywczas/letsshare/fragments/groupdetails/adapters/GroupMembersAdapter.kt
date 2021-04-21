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

    override fun areItemsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean =
        oldItem.id == newItem.id &&
                oldItem.email == newItem.email &&
                oldItem.share == newItem.share &&
                oldItem.expenses == newItem.expenses &&
                oldItem.owesOrOver == newItem.owesOrOver &&
                oldItem.difference == newItem.difference
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.memberName)
        private val expenses: TextView = itemView.findViewById(R.id.memberExpenses)
        private val share: TextView = itemView.findViewById(R.id.percentageShare)
        private val owesOrOver: TextView = itemView.findViewById(R.id.owesOrOver)
        private val difference: TextView = itemView.findViewById(R.id.difference)

        fun bindMember(member: GroupMemberDomain) {
            name.text = member.name
            expenses.text = String.format(Locale.UK, "%.2f %s", member.expenses, currency)
            share.text = String.format(Locale.UK, "%.2f%s", member.share, "%")
            owesOrOver.text = itemView.context.getString(member.owesOrOver)
            difference.text = String.format(Locale.UK, "%.2f %s", member.difference, currency)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_group_members, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindMember(getItem(position))

}