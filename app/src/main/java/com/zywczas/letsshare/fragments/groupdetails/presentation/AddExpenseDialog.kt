package com.zywczas.letsshare.fragments.groupdetails.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.DialogAddExpenseBinding
import com.zywczas.letsshare.utils.autoRelease

class AddExpenseDialog : DialogFragment() {

    private val viewModel: GroupDetailsViewModel by viewModels({ requireParentFragment() })
    private var binding: DialogAddExpenseBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancel.setOnClickListener { dismiss() }
        binding.confirm.setOnClickListener { addExpense() }
    }

    private fun addExpense() {
        when {
            binding.name.text.toString().isBlank() -> binding.nameFrame.error =  getString(R.string.provide_name)
            binding.amount.text.toString().isBlank() -> binding.amountFrame.error = getString(R.string.provide_value)
            else -> {
                viewModel.addExpense(binding.name.text.toString(), binding.amount.text.toString().toBigDecimal())
                dismiss()
            }
        }
    }

}