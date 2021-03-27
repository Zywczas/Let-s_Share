package com.zywczas.letsshare.fragments.groups.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.groups.domain.GroupsRepository
import com.zywczas.letsshare.model.Group
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val repository: GroupsRepository
) : BaseViewModel() {

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> = _groups

//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    private fun onResume() {
//        viewModelScope.launch(dispatchersIO) { getGroups() }
//    }

    suspend fun getGroups() {
        withContext(dispatchersIO){
            if (sessionManager.isNetworkAvailable()) {
                showProgressBar(true)
                val groupsList = repository.getGroups()
                if (groupsList != null) {
                    _groups.postValue(groupsList!!)
                    showProgressBar(false)
                } else {
                    postMessage(R.string.cant_get_groups)
                    showProgressBar(false)
                }
            } else {
                postMessage(R.string.connection_problem)
            }
        }
    }

    suspend fun addGroup(name: String, currency: String) {
        withContext(dispatchersIO){
            if (name.isNotEmpty()){
                if (sessionManager.isNetworkAvailable()) {
                    showProgressBar(true)
                    postMessage(repository.addGroup(name, currency))
                    showProgressBar(false)
                    getGroups()         //todo nie wiem czy tu nie za szybko bedzie pobierac grupy, czy zdaz sie zapisac w bazie, zanim zacznie je pobierac, jak dam pozniej nasluchiwanie bazy to nie bedzie
                    //trzeba tej funkcji tutaj wywolywac
                } else { postMessage(R.string.connection_problem) }
            } else { postMessage(R.string.no_group_name) }
        }
    }

}