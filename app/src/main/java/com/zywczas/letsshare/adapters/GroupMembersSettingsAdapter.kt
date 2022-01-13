package com.zywczas.letsshare.adapters

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
import com.zywczas.letsshare.models.GroupMember
import kotlinx.coroutines.*
import java.math.BigDecimal
import java.util.*

class GroupMembersSettingsAdapter(
    private val lifecycle: Lifecycle,
    private val textDebounce: Long,
    private val onSplitChangeAction: (String, BigDecimal) -> Unit
) : ListAdapter<GroupMember, GroupMembersSettingsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<GroupMember>() {

    override fun areItemsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean =
        oldItem.id == newItem.id &&
            oldItem.share.toString() == newItem.share.toString()
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LifecycleObserver {

        init {
            lifecycle.addObserver(this)
        }

        private val name: TextView = itemView.findViewById(R.id.memberName)
        private val split: TextInputEditText = itemView.findViewById(R.id.split)

        private var updatedPercentageJob: Job? = null
        private val coroutineScope = CoroutineScope(Dispatchers.Main)

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            updatedPercentageJob?.cancel()
        }

        fun bindMember(member: GroupMember) {
            name.text = member.name
            split.setText(String.format(Locale.UK, "%.2f", member.share))
            split.doOnTextChanged { text, _, _, _ ->
                updatedPercentageJob?.cancel()
                updatedPercentageJob = coroutineScope.launch {
                    delay(textDebounce)
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_members_settings, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindMember(getItem(position))

}