package com.zywczas.letsshare.fragments.groupsettings.adapters

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
import java.util.*

class GroupMembersSettingsAdapter(
    private val lifecycle: Lifecycle,
    private val onSplitChangeAction: (String, BigDecimal) -> Unit
) : ListAdapter<GroupMemberDomain, GroupMembersSettingsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<GroupMemberDomain>() {

    override fun areItemsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GroupMemberDomain, newItem: GroupMemberDomain): Boolean =
        oldItem.id == newItem.id &&
            oldItem.share.toString() == newItem.share.toString()
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LifecycleObserver {
//todo sprawdzic co sie stanie jak bede miec 10 czlonkow i niektorzy beda wystawac za ekran i wtedy zamkne fragment, czy nie bedzie crasha, jak lifecycle sprobuje wywolac onDestroy na elemencie ktory jest destroyed
        //moze dac usuwanie obserwatora w onDetachedFromRecyclerView
        init {
            lifecycle.addObserver(this)
        }

        private val name: TextView = itemView.findViewById(R.id.memberName)
        private val split: TextInputEditText = itemView.findViewById(R.id.split)

        private var newSplitJob: Job? = null
        private val coroutineScope = CoroutineScope(Dispatchers.Main) //todo sprawdzic czy tu ma byc main czy IO i dac pozniej w konstruktorze jak

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            newSplitJob?.cancel()
        }

        fun bindMember(member: GroupMemberDomain) {
            name.text = member.name
            split.setText(String.format(Locale.UK, "%.2f", member.share))
            split.doOnTextChanged { text, _, _, _ ->
                newSplitJob?.cancel()
                newSplitJob = coroutineScope.launch {
                    delay(500L) //todo dac to pozniej w konstruktorze
                    text?.let {
                        var splitText = it.toString()
                        if (splitText.isEmpty()) { splitText = "0.00" }
                        onSplitChangeAction(member.id, splitText.toBigDecimal())
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