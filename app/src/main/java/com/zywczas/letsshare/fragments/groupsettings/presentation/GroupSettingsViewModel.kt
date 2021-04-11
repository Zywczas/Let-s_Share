package com.zywczas.letsshare.fragments.groupsettings.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.fragments.groupsettings.domain.GroupSettingsRepository
import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMemberDomain
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class GroupSettingsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupSettingsRepository
) : BaseViewModel() {

    init {
        logD("init") //todo usunac jak zaczne wstrzykiwac view model w dialogi
        viewModelScope.launch { getMembers() }
    }

    private val _members = MutableLiveData<List<GroupMemberDomain>>()
    val members: LiveData<List<GroupMemberDomain>> = _members

    private val _friendsLiveData = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friendsLiveData

    val totalPercentage: LiveData<String> = Transformations.switchMap(_members){ members ->
        liveData(dispatchersIO){
            var totalPercentageTemp = BigDecimal("0.00")
            members.forEach { member ->
                totalPercentageTemp = totalPercentageTemp.plus(member.percentage_share)
            }
            emit(String.format(Locale.getDefault(), "%.2f%s", totalPercentageTemp, "%"))
        }
    }

    private val _isPercentageChanged = MutableLiveData<Boolean>()

    private suspend fun getMembers() {
        withContext(dispatchersIO){
            showProgressBar(true)
            repository.getMembers()?.let{ _members.postValue(it) }
                ?: kotlin.run { postMessage(R.string.cant_get_group_members) }
            showProgressBar(false)
        }
    }

    suspend fun getFriends(){
        withContext(dispatchersIO){ _friendsLiveData.postValue(repository.getFriends()) }
    }

//todo tu trzeba jeszcze dodac ustawianie splitow i zatwierdzenie dopiero jak jest ok
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

    suspend fun updatePercentage(email: String, share: BigDecimal){
        withContext(dispatchersIO){
            _members.value?.let { members ->
                members.first { it.email == email }.percentage_share =
                    share.setScale(2, BigDecimal.ROUND_HALF_UP)
                _members.postValue(members)
            }
        }
    }

    suspend fun setEqualSplits(){
        withContext(dispatchersIO){
            val membersTemp = _members.value
            val numberOfMembers = membersTemp?.count() ?: 0
            if (numberOfMembers != 0){
                val newSplit = BigDecimal(100).divide(numberOfMembers.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP)
                membersTemp?.let {
                    val updatedMembers = mutableListOf<GroupMemberDomain>()
                    it.forEach { member ->
                        val newMemberRef = GroupMemberDomain(member.name, member.email, member.expenses, newSplit)
                        updatedMembers.add(newMemberRef)
                    }
                    _members.postValue(updatedMembers)
                }
            }
        }
    }

}