package com.zywczas.letsshare.fragments.groupsettings.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.fragments.BaseViewModel
import com.zywczas.letsshare.fragments.groupsettings.domain.GroupSettingsRepository
import com.zywczas.letsshare.models.Friend
import com.zywczas.letsshare.models.GroupMember
import com.zywczas.letsshare.models.GroupMonth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class GroupSettingsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupSettingsRepository,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    private val _members = MutableLiveData<List<GroupMember>>()
    val members: LiveData<List<GroupMember>> = _members

    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friends

    private val _goToGroupsListFragment = MutableLiveData<Boolean>()
    val goToGroupsListFragment: LiveData<Boolean> = _goToGroupsListFragment

    private var month = GroupMonth()

    val totalPercentage: LiveData<String> = Transformations.switchMap(members){ members ->
        liveData(dispatchersIO){
            var totalPercentageTemp = BigDecimal("0.00")
            members.forEach { totalPercentageTemp = totalPercentageTemp.plus(it.share) }
            emit(String.format(Locale.UK, "%.2f%s", totalPercentageTemp, "%"))
        }
    }

    private val _areSettingsChanged = MutableLiveData<Boolean>()
    val areSettingsChanged: LiveData<Boolean> = _areSettingsChanged

    fun getMonthSettings(month: GroupMonth){
        viewModelScope.launch(dispatchersIO){
            this@GroupSettingsViewModel.month = month
            getMembers()
        }
    }

    private suspend fun getMembers() {
        showProgressBar(true)
        repository.getMembers(month.id)?.let{ _members.postValue(it) }
            ?: postMessage(R.string.cant_get_group_members)
        showProgressBar(false)
    }

    fun getFriends(){
        viewModelScope.launch(dispatchersIO){
            showProgressBar(true)
            _friends.postValue(repository.getFriends())
            showProgressBar(false)
        }
    }

    fun addNewMember(friend: Friend){
        viewModelScope.launch(dispatchersIO){
            if (month.isSettledUp.not()){
                showProgressBar(true)
                when(isFriendInTheGroupAlready(friend.id)){
                    null -> postMessage(R.string.something_wrong)
                    true -> postMessage(R.string.member_exists)
                    false -> {
                        when(repository.isFriendIn5GroupsAlready(friend.id)){
                            null -> postMessage(R.string.something_wrong)
                            true -> postMessage(R.string.friend_in_too_many_groups)
                            false -> {
                                repository.addMemberIfBelow7PeopleInGroup(month.id, friend)?.let { error -> postMessage(error)}
                                    ?: kotlin.run {
                                        getMembers()
                                        _areSettingsChanged.postValue(true)
                                    }
                            }
                        }
                    }
                }
                showProgressBar(false)
            } else {
                postMessage(R.string.cant_operate_on_settled_up_month)
            }
        }
    }

    private fun isFriendInTheGroupAlready(newMemberId: String): Boolean? =
        members.value?.any{ it.id == newMemberId }

    fun deleteMember(friend: Friend){
        viewModelScope.launch(dispatchersIO){
            showProgressBar(true)
            when {
                month.isSettledUp -> postMessage(R.string.cant_operate_on_settled_up_month)
                sessionManager.isNetworkAvailable().not() -> postMessage(R.string.connection_problem)
                else -> {
                    showProgressBar(true)
                    val member = members.value!!.first { it.id == friend.id }
                    repository.removeMemberOrCloseGroup(month.id, member.id)?.let { error ->
                        postMessage(error)
                        showProgressBar(false)
                    } ?: kotlin.run {
                        if (hasUserLeftTheGroup(friend.id)){
                            _goToGroupsListFragment.postValue(true)
                        } else {
                            getMembers()
                            _areSettingsChanged.postValue(true)
                        }
                    }
                }
            }
        }
    }

    private suspend fun hasUserLeftTheGroup(memberId: String): Boolean = memberId == repository.userId()

    fun updatePercentage(memberId: String, share: BigDecimal){
        viewModelScope.launch(dispatchersIO){
            members.value?.let { members ->
                members.first { it.id == memberId }.share = share.setScale(2, BigDecimal.ROUND_HALF_UP)
                _members.postValue(members)
                _areSettingsChanged.postValue(true)
            }
        }
    }

    fun setEqualSplits(){
        viewModelScope.launch(dispatchersIO){
            val membersTemp = members.value
            val numberOfMembers = membersTemp?.size ?: 0
            if (numberOfMembers != 0){
                val newSplit = BigDecimal(100).divide(numberOfMembers.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP)
                membersTemp?.let { members ->
                    val newMembersRef = members.map {
                        GroupMember(
                            id = it.id,
                            name = it.name,
                            share = newSplit
                        )}
                    _members.postValue(newMembersRef)
                    _areSettingsChanged.postValue(true)
                }
            }
        }
    }

    fun saveSplits(){
        viewModelScope.launch(dispatchersIO){
            showProgressBar(true)
            when {
                month.isSettledUp -> postMessage(R.string.cant_operate_on_settled_up_month)
                totalPercentage.value.toString() != "100.00%" -> postMessage(R.string.percentage_not_100)
                sessionManager.isNetworkAvailable().not() -> postMessage(R.string.connection_problem)
                members.value == null -> postMessage(R.string.cant_get_group_members)
                else -> {
                    repository.saveSplits(month.id, members.value!!)?.let { error -> postMessage(error) }
                        ?: _areSettingsChanged.postValue(false)
                }
            }
            showProgressBar(false)
        }
    }

}