package com.zywczas.letsshare.fragments.groupsettings.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.zywczas.letsshare.R
import com.zywczas.letsshare.model.GroupMemberDomain
import kotlinx.coroutines.*
import java.math.BigDecimal

class GroupMembersSettingsAdapter(
    private val lifecycle: Lifecycle,
    private val onSplitChangeAction: (String, BigDecimal) -> Unit
) : ListAdapter<GroupMemberDomain, GroupMembersSettingsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<GroupMemberDomain>() {

    override fun areItemsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean = oldItem.email == newItem.email

    override fun areContentsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean =
        oldItem.email == newItem.email && oldItem.percentage_share == newItem.percentage_share
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LifecycleObserver {

        init {
            lifecycle.addObserver(this)
        }

        private val name: TextView = itemView.findViewById(R.id.memberName)
        private val share: TextView = itemView.findViewById(R.id.percentageShare)
        private val newSplit: TextInputEditText = itemView.findViewById(R.id.newSplit)

        private var searchJob: Job? = null
        private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main) //todo sprawdzic czy tu ma byc main czy IO i dac pozniej w konstruktorze jak

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            searchJob?.cancel()
        }

        @SuppressLint("SetTextI18n")
        fun bindMember(member: GroupMemberDomain) {
            name.text = member.name
            share.text = "${member.percentage_share} %"
            newSplit.text
            newSplit.doOnTextChanged { text, _, _, _ ->
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    text?.let {
                        delay(500L) //todo dac to pozniej w konstruktorze
                        onSplitChangeAction(member.email, text.toString().toBigDecimal())
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_group_members_settings, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindMember(getItem(position))

}