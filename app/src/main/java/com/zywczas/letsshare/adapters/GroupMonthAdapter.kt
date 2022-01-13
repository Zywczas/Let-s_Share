package com.zywczas.letsshare.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.extentions.getColorFromAttr
import com.zywczas.letsshare.models.GroupMonth
import java.util.*

class GroupMonthAdapter(
    private val currency: String,
    private val onClick: (GroupMonth) -> Unit
) : ListAdapter<GroupMonth, GroupMonthAdapter.ViewHolder>(object : DiffUtil.ItemCallback<GroupMonth>() {

    override fun areItemsTheSame(oldItem: GroupMonth, newItem: GroupMonth): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GroupMonth, newItem: GroupMonth): Boolean =
        oldItem.id == newItem.id && oldItem.isSettledUp == newItem.isSettledUp
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val expenses: TextView = itemView.findViewById(R.id.expenses)
        private val isSettledUp: ImageView = itemView.findViewById(R.id.isSettledUp)

        fun bindMonth(month: GroupMonth) {
            name.text = month.id
            expenses.text = String.format(Locale.UK, "%.2f $currency", month.totalExpenses)
            if (month.isSettledUp){
                isSettledUp.imageTintList = ColorStateList.valueOf(itemView.context.getColorFromAttr(R.attr.checkColor))
            }
            itemView.setOnClickListener { onClick(month) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_month, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindMonth(getItem(position))

}