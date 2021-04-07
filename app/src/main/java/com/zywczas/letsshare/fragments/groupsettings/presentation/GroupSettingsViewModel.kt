package com.zywczas.letsshare.fragments.groupsettings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.fragments.groupsettings.domain.GroupSettingsRepository
import com.zywczas.letsshare.model.GroupMemberDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupSettingsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupSettingsRepository
) : BaseViewModel() {

    private val _members = MutableLiveData<List<GroupMemberDomain>>()
    val members: LiveData<List<GroupMemberDomain>> = _members

    suspend fun getMembers() {
        withContext(dispatchersIO){
            showProgressBar(true)
            repository.getMembers()?.let{ _members.postValue(it) }
                ?: kotlin.run { postMessage(R.string.cant_get_group_members) }
            showProgressBar(false)
        }
    }

}