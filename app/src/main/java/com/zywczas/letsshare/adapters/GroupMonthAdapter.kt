package com.zywczas.letsshare.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.model.GroupMonthDomain
import java.util.*

class GroupMonthAdapter(
    private val currency: String,
    private val onClick: (GroupMonthDomain) -> Unit
) : ListAdapter<GroupMonthDomain, GroupMonthAdapter.ViewHolder>(object : DiffUtil.ItemCallback<GroupMonthDomain>() {

    override fun areItemsTheSame(oldItem: GroupMonthDomain, newItem: GroupMonthDomain): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GroupMonthDomain, newItem: GroupMonthDomain): Boolean =
        oldItem.id == newItem.id && oldItem.isSettledUp == newItem.isSettledUp
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val expenses: TextView = itemView.findViewById(R.id.expenses)
        private val isSettledUp: TextView = itemView.findViewById(R.id.isSettledUp)

        fun bindMonth(month: GroupMonthDomain) {
            name.text = month.id
            expenses.text = String.format(Locale.UK, "%.2f $currency", month.totalExpenses)
            isSettledUp.text = if (month.isSettledUp) { itemView.context.getString(R.string.yes) }
                else { itemView.context.getString(R.string.no) }

            itemView.setOnClickListener { onClick(month) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_group_month, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindMonth(getItem(position))

}