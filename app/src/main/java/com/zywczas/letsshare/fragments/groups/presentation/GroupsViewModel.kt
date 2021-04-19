package com.zywczas.letsshare.fragments.groups.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.groups.domain.GroupsRepository
import com.zywczas.letsshare.model.Group
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val repository: GroupsRepository
) : BaseViewModel() {

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> = _groups

    suspend fun getGroups() {
        withContext(dispatchersIO) {
            if (sessionManager.isNetworkAvailable()) {
                showProgressBar(true)
                repository.getGroups()?.let { _groups.postValue(it) } ?: postMessage(R.string.cant_get_groups)
                showProgressBar(false)
            } else {
                postMessage(R.string.connection_problem)
            }
        }
    }

    suspend fun addGroup(name: String, currency: String) {
        withContext(dispatchersIO) {
            showProgressBar(true)
            when {
                name.isBlank() -> postMessage(R.string.no_group_name)
                sessionManager.isNetworkAvailable().not() -> postMessage(R.string.connection_problem)
                else -> {
                    postMessage(repository.addGroupIfUserIsInLessThan10Groups(name, currency)) //todo jak dam pozniej nasluchiwanie bazy to bez wiadomosci
                    getGroups() //todo jak dam pozniej nasluchiwanie bazy to nie bedzie tego
                }
            }
//            showProgressBar(false) //todo jak dam pozniej nasluchiwanie bazy to moze to bedzie potrzebne
        }
    }

    suspend fun saveCurrentlyOpenGroupId(groupId: String) {
        withContext(dispatchersIO) { repository.saveCurrentlyOpenGroupId(groupId) }
    }

}