package com.zywczas.letsshare.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.models.firestore.GroupFire

class GroupsAdapter (
    private val itemClick: (GroupFire) -> Unit
) : ListAdapter<GroupFire, GroupsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<GroupFire>() {

    override fun areItemsTheSame(oldItem: GroupFire, newItem: GroupFire): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GroupFire, newItem: GroupFire): Boolean = oldItem.id == newItem.id
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.nameGroupsListItem)

        fun bindGroup(group: GroupFire) {
            name.text = group.name
            itemView.setOnClickListener { itemClick(group) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_groups, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindGroup(getItem(position))

}