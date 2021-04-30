package com.zywczas.letsshare.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.models.ExpenseDomain
import java.util.*

class ExpensesAdapter(private val currency: String) : ListAdapter<ExpenseDomain, ExpensesAdapter.ViewHolder>(object : DiffUtil.ItemCallback<ExpenseDomain>() {

    override fun areItemsTheSame(oldItem: ExpenseDomain, newItem: ExpenseDomain): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ExpenseDomain, newItem: ExpenseDomain): Boolean = oldItem.id == newItem.id
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val expenseName: TextView = itemView.findViewById(R.id.expenseName)
        private val date: TextView = itemView.findViewById(R.id.date)
        private val payeeName: TextView = itemView.findViewById(R.id.payeeName)
        private val value: TextView = itemView.findViewById(R.id.value)
        private val currencyView: TextView = itemView.findViewById(R.id.currency)

        fun bindExpense(expense: ExpenseDomain) {
            expenseName.text = expense.name
            date.text = expense.dateCreated
            payeeName.text = expense.payeeName
            value.text = String.format(Locale.UK, "%.2f", expense.value)
            currencyView.text = currency
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expenses, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindExpense(getItem(position))

}