package com.zywczas.letsshare.fragmentgroupdetails.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leinardi.android.speeddial.SpeedDialView
import com.zywczas.letsshare.R
import com.zywczas.letsshare.databinding.FragmentFriendsBinding
import com.zywczas.letsshare.databinding.FragmentGroupDetailsBinding
import com.zywczas.letsshare.fragmentgroupdetails.adapters.GroupMembersAdapter
import com.zywczas.letsshare.utils.COLLECTION_USERS
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.showToast
import javax.inject.Inject

class GroupDetailsFragment @Inject constructor(private val viewModel: GroupDetailsViewModel): Fragment() {

    private var binding: FragmentGroupDetailsBinding by autoRelease()
    private val args: GroupDetailsFragmentArgs by navArgs()
    private val membersAdapter by lazy { GroupMembersAdapter(args.group.currency) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            membersAdapterXML = membersAdapter
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.button.setOnClickListener {
            lifecycleScope.launchWhenResumed { viewModel.getMembers(args.group.id) }
        }

        setupToolbar()
        setupObservers()
        setupSpeedDialMenu()
        setupOnClickListeners()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupToolbar(){
        binding.toolbar.title = args.group.name
        binding.toolbar.setupWithNavController(findNavController())
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showToast(it) }
        viewModel.members.observe(viewLifecycleOwner){ membersAdapter.submitList(it.toMutableList()) }
    }

    private fun setupSpeedDialMenu(){

    }

    private fun setupOnClickListeners(){
        setupSpeedDialMainBtnClick()
//        setupSpeedDialMenuClick()
    }

    private fun setupSpeedDialMainBtnClick(){
        binding.speedDial.setOnChangeListener(object : SpeedDialView.OnChangeListener{
            override fun onMainActionSelected(): Boolean {
                return false
            }
            override fun onToggleChanged(isOpen: Boolean) {
                dimOrRestoreBackground(isOpen)
            }
        })
    }

    private fun dimOrRestoreBackground(isDialOpen : Boolean){
        val window = requireActivity().window
        if (isDialOpen){
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.purple_700_alpha03)
            binding.mainLayout.alpha = 0.3F
        } else {
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.purple_700) //todo poprawic te kolory, zeby bralo ten z theme
            binding.mainLayout.alpha = 1F
        }
    }

}