package com.zywczas.letsshare.fragments.historydetails.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.zywczas.letsshare.databinding.DialogSettleUpBinding
import com.zywczas.letsshare.utils.autoRelease

class SettleUpDialog : DialogFragment() {

    private val viewModel: HistoryDetailsViewModel by viewModels({ requireParentFragment() })
    private var binding: DialogSettleUpBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSettleUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancel.setOnClickListener { dismiss() }
        binding.confirm.setOnClickListener {
            viewModel.settleUp()
            dismiss()
        }
    }

}