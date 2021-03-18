package com.zywczas.letsshare.fragmentgroups.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.fragmentgroups.domain.GroupsRepository
import com.zywczas.letsshare.model.Group
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val repository: GroupsRepository
) : BaseViewModel(), LifecycleObserver {

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> = _groups

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        viewModelScope.launch(dispatchersIO) { getGroups() }
    }

    private suspend fun getGroups() = withContext(dispatchersIO){
        if (sessionManager.isNetworkAvailable()) {  }
        else { postMessage(R.string.connection_problem) }
    }

    suspend fun addGroup(name: String, currency: String) {
        withContext(dispatchersIO){
            if (sessionManager.isNetworkAvailable()) { postMessage(repository.addGroup(name, currency)) }
            else { postMessage(R.string.connection_problem) }
        }
    }

}