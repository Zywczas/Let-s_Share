package com.zywczas.letsshare.fragments.groupdetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.groupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupDetailsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupDetailsRepository
) : BaseViewModel() {

    init {
        logD("init")
    }

    private val _members = MutableLiveData<List<GroupMember>>()
    val members: LiveData<List<GroupMember>> = _members

    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friends

    suspend fun getMembers(groupId: String) {
        withContext(dispatchersIO){ //todo da gdzie indziej tak samo :)
            showProgressBar(true)
            repository.getMembers(groupId)?.let{ _members.postValue(it) }
                ?: kotlin.run { postMessage(R.string.cant_get_group_members) }
            showProgressBar(false)
        }
    }

    suspend fun getFriends(){
        withContext(dispatchersIO){ _friends.postValue(repository.getFriends()) }
    }

    suspend fun addNewMember(friend: Friend, groupId: String){
        withContext(dispatchersIO){
            showProgressBar(true)
            repository.addNewMemberIfBelow7InGroup(friend.toGroupMember(), groupId)?.let { error ->
                postMessage(error)
                showProgressBar(false)
            } ?: kotlin.run { getMembers(groupId) }
        }
    }

    private fun Friend.toGroupMember() = GroupMember(name, email)

    suspend fun addNewExpense(groupId: String, name: String, amount: Double){
        withContext(dispatchersIO){
            //todo
        }
    }

}