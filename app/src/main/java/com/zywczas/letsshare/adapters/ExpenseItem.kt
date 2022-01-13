package com.zywczas.letsshare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.diff.DiffCallback
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.ItemExpensesBinding
import com.zywczas.letsshare.models.Expense
import java.util.*

class ExpenseItem(private val expense: Expense, private val currency: String) : AbstractBindingItem<ItemExpensesBinding>()  {

    override val type: Int = R.id.expenseItem

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemExpensesBinding =
        ItemExpensesBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ItemExpensesBinding, payloads: List<Any>) {
        binding.expenseName.text = expense.name
        binding.date.text = expense.dateCreated
        binding.payeeName.text = expense.payeeName
        binding.value.text = String.format(Locale.UK, "%.2f", expense.value)
        binding.currency.text = currency
    }

    class DiffUtil : DiffCallback<ExpenseItem> {

        override fun areItemsTheSame(oldItem: ExpenseItem, newItem: ExpenseItem): Boolean = oldItem.expense.id == newItem.expense.id

        override fun areContentsTheSame(oldItem: ExpenseItem, newItem: ExpenseItem): Boolean = oldItem.expense.id == newItem.expense.id

        override fun getChangePayload(oldItem: ExpenseItem, oldItemPosition: Int, newItem: ExpenseItem, newItemPosition: Int): Any = newItem

    }

}