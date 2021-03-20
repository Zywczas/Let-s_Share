package com.zywczas.letsshare.fragmentgroupdetails.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentFriendsBinding
import com.zywczas.letsshare.databinding.FragmentGroupDetailsBinding
import com.zywczas.letsshare.utils.COLLECTION_USERS
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.showToast
import javax.inject.Inject

class GroupDetailsFragment @Inject constructor(private val viewModel: GroupDetailsViewModel): Fragment() {

    private var binding: FragmentGroupDetailsBinding by autoRelease()
    private val args: GroupDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        setupToolbar()
        setupRecyclers()
        setupObservers()
        setupOnClickListeners()
    }

    private fun setupToolbar(){
        binding.toolbar.title = args.group.name
    }

    private fun setupRecyclers(){

    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
    }

    private fun setupOnClickListeners(){

    }

}