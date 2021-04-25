package com.zywczas.letsshare.fragments.groups.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.groups.domain.GroupsRepository
import com.zywczas.letsshare.model.Group
import com.zywczas.letsshare.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val repository: GroupsRepository
) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    private val user: LiveData<User> = _user

    val groups: LiveData<List<Group>> = Transformations.switchMap(user){ user ->
        liveData(dispatchersIO){
            showProgressBar(true)
            repository.getGroups(user.groupsIds)?.let { emit(it) } ?: postMessage(R.string.cant_get_groups)
            showProgressBar(false)
        }
    }

    fun getUserGroups() {
        viewModelScope.launch(dispatchersIO) {
            if (sessionManager.isNetworkAvailable()) {
                repository.getUser()
                    .buffer(Channel.CONFLATED)
                    .catch { postMessage(R.string.cant_get_groups) }
                    .collect { _user.postValue(it) }
            } else {
                postMessage(R.string.connection_problem)
            }
        }
    }

    fun addGroup(name: String, currency: String) {
        viewModelScope.launch(dispatchersIO) {
            showProgressBar(true)
            when {
                name.isBlank() -> postMessage(R.string.no_group_name)
                sessionManager.isNetworkAvailable().not() -> postMessage(R.string.connection_problem)
                else -> {
                    when(repository.isUserIn5GroupsAlready()){
                        null -> postMessage(R.string.something_wrong)
                        true -> postMessage(R.string.too_many_groups)
                        false -> repository.addGroup(name, currency)?.let { error -> postMessage(error) }
                    }
                }
            }
            showProgressBar(false)
        }
    }

    fun saveCurrentlyOpenGroupId(groupId: String) {
        viewModelScope.launch(dispatchersIO) { repository.saveCurrentlyOpenGroupId(groupId) }
    }

}