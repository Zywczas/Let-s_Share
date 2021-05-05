package com.zywczas.letsshare.fragments.register.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.zywczas.letsshare.databinding.DialogInfoBinding
import com.zywczas.letsshare.utils.autoRelease

class InfoDialog : DialogFragment() {

    private var binding: DialogInfoBinding by autoRelease()
    private val args: InfoDialogArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = args.title
        binding.confirm.setOnClickListener { dismiss() }
    }

}