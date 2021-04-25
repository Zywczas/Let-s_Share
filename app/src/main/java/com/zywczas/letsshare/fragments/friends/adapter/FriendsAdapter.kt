package com.zywczas.letsshare.fragments.friends.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.model.Friend

class FriendsAdapter(private val onClick: (Friend) -> Unit = {}) :
    ListAdapter<Friend, FriendsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Friend>() {

    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.id == newItem.id
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val email: TextView = itemView.findViewById(R.id.email)

        fun bindFriend(friend: Friend) {
            name.text = friend.name
            email.text = friend.email
            itemView.setOnClickListener { onClick(friend) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_friends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindFriend(getItem(position))

}