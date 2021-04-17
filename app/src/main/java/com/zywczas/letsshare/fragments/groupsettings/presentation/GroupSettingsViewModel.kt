package com.zywczas.letsshare.fragments.groupsettings.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.fragments.groupsettings.domain.GroupSettingsRepository
import com.zywczas.letsshare.model.Friend
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

    private val _members = MutableLiveData<MutableList<GroupMemberDomain>>()
    val members: LiveData<MutableList<GroupMemberDomain>> = _members

    private val _friends = MutableLiveData<MutableList<Friend>>()
    val friends: LiveData<MutableList<Friend>> = _friends

    val totalPercentage: LiveData<String> = Transformations.switchMap(members){ members ->
        liveData(dispatchersIO){
            var totalPercentageTemp = BigDecimal("0.00")
            members.forEach { totalPercentageTemp = totalPercentageTemp.plus(it.percentageShare) }
            emit(String.format(Locale.UK, "%.2f%s", totalPercentageTemp, "%"))
        }
    }

    private val _isPercentageChanged = MutableLiveData<Boolean>()
    val isPercentageChanged: LiveData<Boolean> = _isPercentageChanged

    private suspend fun getMembers() {
        withContext(dispatchersIO){
            showProgressBar(true)
            repository.getMembers()?.let{ _members.postValue(it.toMutableList()) }
                ?: kotlin.run { postMessage(R.string.cant_get_group_members) }
            showProgressBar(false)
        }
    }

    suspend fun getFriends(){
        withContext(dispatchersIO){ _friends.postValue(repository.getFriends().toMutableList()) }
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
                            repository.addMemberIfBelow7InGroup(friend)
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

    suspend fun updatePercentage(memberEmail: String, share: BigDecimal){
        withContext(dispatchersIO){
            members.value?.let { members ->
                members.first { it.email == memberEmail }.percentageShare =
                    share.setScale(2, BigDecimal.ROUND_HALF_UP)
                _members.postValue(members)
                _isPercentageChanged.postValue(true)
            }
        }
    }

    suspend fun setEqualSplits(){
        withContext(dispatchersIO){
            val membersTemp = members.value
            val numberOfMembers = membersTemp?.count() ?: 0
            if (numberOfMembers != 0){
                val newSplit = BigDecimal(100).divide(numberOfMembers.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP)
                membersTemp?.let { members ->
                    val newMembersRef = members
                        .map { GroupMemberDomain(it.name, it.email, it.expenses, newSplit) }
                        .toMutableList()
                    _members.postValue(newMembersRef)
                    _isPercentageChanged.postValue(true)
                }
            }
        }
    }

    suspend fun saveSplits(){
        withContext(dispatchersIO){
            if (totalPercentage.value.toString() == "100.00%") {
                showProgressBar(true)
                postMessage(repository.saveSplits(members.value!!))
                showProgressBar(false)
            }
            else { postMessage(R.string.percentage_not_100) }
        }
    }

}