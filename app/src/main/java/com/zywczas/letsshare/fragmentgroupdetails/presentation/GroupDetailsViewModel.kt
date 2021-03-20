package com.zywczas.letsshare.fragmentgroupdetails.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragmentgroupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.model.GroupMember
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupDetailsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupDetailsRepository
) : BaseViewModel(), LifecycleObserver {

    private val _members = MutableLiveData<List<GroupMember>>()
    val members: LiveData<List<GroupMember>> = _members

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        viewModelScope.launch(dispatchersIO) { getMembers() }
    }

    private suspend fun getMembers() = withContext(dispatchersIO){
        showProgressBar(true)

        showProgressBar(false)
    }

}