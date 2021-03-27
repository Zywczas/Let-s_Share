package com.zywczas.letsshare.fragments.groupdetails.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.DialogAddExpenseBinding
import com.zywczas.letsshare.utils.GROUP_ID_KEY
import com.zywczas.letsshare.utils.autoRelease

class AddExpenseDialog : DialogFragment() {

    //todo zamienic pozniej na safe args, jak ogarne view model
    private val viewModel: GroupDetailsViewModel by viewModels({ requireParentFragment() })
    private var binding: DialogAddExpenseBinding by autoRelease()
    private val groupId by lazy { requireArguments().getString(GROUP_ID_KEY)!! } //todo zamienic pozniej na safe args, jak ogarne view model

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
        binding.ok.setOnClickListener { verifyValuesAndAddExpense() }
    }

    private fun verifyValuesAndAddExpense() {
        when { //todo dac tutaj pozniej cofanie na normaly background jak jest wartosc
            binding.name.text.toString().isBlank() -> binding.name.setBackgroundResource(R.drawable.edittext_red_stroke)
            binding.amout.text.toString().isBlank() -> binding.amout.setBackgroundResource(R.drawable.edittext_red_stroke)
            else -> lifecycleScope.launchWhenResumed {
                viewModel.addNewExpenseToThisMonth(
                    groupId,
                    binding.name.text.toString(),
                    binding.amout.text.toString().toBigDecimal()
                )
                dismiss()
            }
        }
    }

}