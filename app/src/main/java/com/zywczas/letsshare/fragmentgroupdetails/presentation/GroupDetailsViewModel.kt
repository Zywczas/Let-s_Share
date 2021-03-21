package com.zywczas.letsshare.fragmentgroupdetails.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragmentgroupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupDetailsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupDetailsRepository
) : BaseViewModel() {

    init {
        logD("view model jest tworzony")
    }

    private val _members = MutableLiveData<List<GroupMember>>()
    val members: LiveData<List<GroupMember>> = _members

    suspend fun getMembers(groupId: String) {
        withContext(dispatchersIO){
            showProgressBar(true)
            val membersList = repository.getMembers(groupId)
            if (membersList != null) { _members.postValue(membersList!!) }
            else { postMessage(R.string.cant_get_group_members) }
            showProgressBar(false)
        }
    }

}