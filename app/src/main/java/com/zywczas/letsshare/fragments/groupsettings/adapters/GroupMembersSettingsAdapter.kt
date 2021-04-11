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
import com.zywczas.letsshare.utils.setSelectionAtTheEnd
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
//todo sprawdzic co sie stanie jak bede miec 10 czlonkow i niektorzy beda wystawac za ekran i wtedy zamkne fragment, czy nie bedzie crasha, jak lifecycle sprobuje wywolac onDestroy na elemencie ktory jest destroyed
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
//todo poprawic pozniej to rozwiazanie bo jak sie kogos ustawi a potem zjedzie na dol i sie wroci to on sie zresetuje obecnie
        @SuppressLint("SetTextI18n")
        fun bindMember(member: GroupMemberDomain) {
            name.text = member.name
            split.setText(member.percentage_share.toString())
            split.setSelectionAtTheEnd()
            split.doOnTextChanged { text, _, _, _ ->
                newSplitJob?.cancel()
                newSplitJob = coroutineScope.launch {
                    delay(500L) //todo dac to pozniej w konstruktorze
                    text?.let {
                        var splitText = it
                        if (splitText.isEmpty()) {
                            splitText = "0"
                        }
                        onSplitChangeAction(member.email, splitText.toString().toBigDecimal())
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