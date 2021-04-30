package com.zywczas.letsshare.fragments.history.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.zywczas.letsshare.adapters.GroupMonthAdapter
import com.zywczas.letsshare.databinding.FragmentHistoryBinding
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.utils.autoRelease
import com.zywczas.letsshare.utils.showSnackbar
import javax.inject.Inject

class HistoryFragment @Inject constructor(viewModelFactory: UniversalViewModelFactory) : Fragment() {

    private val viewModel: HistoryViewModel by viewModels { viewModelFactory }
    private var binding: FragmentHistoryBinding by autoRelease()
    private val args: HistoryFragmentArgs by navArgs()
    private val adapter by lazy { GroupMonthAdapter(args.group.currency){ month ->
        findNavController().navigate(HistoryFragmentDirections.toHistoryDetailsFragment(args.group, month))
    } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMonths()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapterXML = adapter
        }
        binding.toolbar.setupWithNavController(findNavController())
        setupObservers()
    }

    private fun setupObservers(){
        viewModel.message.observe(viewLifecycleOwner){ showSnackbar(it)}
        viewModel.months.observe(viewLifecycleOwner){ adapter.submitList(it.toMutableList()) }
    }

}