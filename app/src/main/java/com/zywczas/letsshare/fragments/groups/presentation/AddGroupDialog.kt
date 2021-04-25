package com.zywczas.letsshare.fragments.groups.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zywczas.letsshare.databinding.DialogAddGroupBinding
import com.zywczas.letsshare.utils.autoRelease

class AddGroupDialog : DialogFragment() {

    private val viewModel: GroupsViewModel by viewModels({ requireParentFragment() })
    private var binding: DialogAddGroupBinding by autoRelease()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancel.setOnClickListener { dismiss() }
        binding.confirm.setOnClickListener { addGroup() }
    }

    private fun addGroup(){
        viewModel.addGroup(binding.name.text.toString(), "zł")
        dismiss()
    }

}