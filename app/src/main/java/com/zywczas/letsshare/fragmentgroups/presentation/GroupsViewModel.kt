package com.zywczas.letsshare.fragmentgroups.presentation

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupsViewModel @Inject constructor() : ViewModel(), LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {

    }

}