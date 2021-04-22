package com.zywczas.letsshare.fragments.settings.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zywczas.letsshare.R
import com.zywczas.letsshare.utils.turnOffOnBackPressed
import javax.inject.Inject

class SettingsFragment @Inject constructor() : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        turnOffOnBackPressed()
    }

}