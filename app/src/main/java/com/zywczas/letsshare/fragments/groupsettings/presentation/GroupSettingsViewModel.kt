package com.zywczas.letsshare.fragments.groupsettings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.fragments.groupsettings.domain.GroupSettingsRepository
import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMemberDomain
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupSettingsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupSettingsRepository
) : BaseViewModel() {

    init {
        logD("init") //todo usunac jak zaczne wstrzykiwac view model w dialogi
    }

    private val _members = MutableLiveData<List<GroupMemberDomain>>()
    val members: LiveData<List<GroupMemberDomain>> = _members

    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friends

    suspend fun getMembers() {
        withContext(dispatchersIO){
            showProgressBar(true)
            repository.getMembers()?.let{ _members.postValue(it) }
                ?: kotlin.run { postMessage(R.string.cant_get_group_members) }
            showProgressBar(false)
        }
    }

    suspend fun getFriends(){
        withContext(dispatchersIO){ _friends.postValue(repository.getFriends()) }
    }

    suspend fun addNewMember(friend: Friend){
        withContext(dispatchersIO){
            showProgressBar(true)
            when(repository.isFriendInTheGroupAlready(friend.email)){
                null -> {
                    postMessage(R.string.something_wrong)
                    showProgressBar(false)
                }
                true -> {
                    postMessage(R.string.member_exists)
                    showProgressBar(false)
                }
                false -> {
                    when(repository.isFriendIn10GroupsAlready(friend.email)){
                        null -> {
                            postMessage(R.string.something_wrong)
                            showProgressBar(false)
                        }
                        true -> {
                            postMessage(R.string.friend_in_too_many_groups)
                            showProgressBar(false)
                        }
                        false -> {
                            repository.addNewMemberIfBelow7InGroup(friend.toGroupMember())
                                ?.let { error ->
                                    postMessage(error)
                                    showProgressBar(false)
                                } ?: kotlin.run { getMembers() }
                        }
                    }
                }
            }
        }
    }

    private fun Friend.toGroupMember() = GroupMember(name, email)

}