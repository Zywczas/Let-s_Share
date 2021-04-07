package com.zywczas.letsshare.fragments.groupdetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.groupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.model.ExpenseDomain
import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMemberDomain
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class GroupDetailsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupDetailsRepository
) : BaseViewModel() {

    init {
        logD("init") //todo usunac jak zaczne wstrzykiwac view model w dialogi
    }

    private val _members = MutableLiveData<List<GroupMemberDomain>>()
    val members: LiveData<List<GroupMemberDomain>> = _members

    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friends

    private val _expenses = MutableLiveData<List<ExpenseDomain>>()
    val expenses: LiveData<List<ExpenseDomain>> = _expenses

    //todo pousuwac wszystkie groupId z funkcji viewmodeli i fragmentow bo zapisuje je do shre prefs, i brac stamtad
    suspend fun getMembers(groupId: String) {
        withContext(dispatchersIO){ //todo da gdzie indziej tak samo :) - czyli ?.let i run
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
            if (repository.isFriendInTheGroupAlready(friend.email, groupId)) {
                postMessage(R.string.member_exists)
                showProgressBar(false)
            }  else {
                when(repository.isFriendIn10GroupsAlready(friend.email)){
                    null -> {
                        postMessage(R.string.cant_verify_friend)
                        showProgressBar(false)
                    }
                    true -> {
                        postMessage(R.string.friend_in_too_many_groups)
                        showProgressBar(false)
                    }
                    else -> {
                        repository.addNewMemberIfBelow7InGroup(friend.toGroupMember(), groupId)
                            ?.let { error ->
                                postMessage(error)
                                showProgressBar(false)
                            } ?: kotlin.run { getMembers(groupId) }
                    }
                }
            }
        }
    }

    private fun Friend.toGroupMember() = GroupMember(name, email)

    suspend fun getExpenses(groupId: String){
        withContext(dispatchersIO){
            showProgressBar(true)
            repository.getExpenses(groupId)?.let { _expenses.postValue(it) }
                ?: kotlin.run { postMessage(R.string.cant_get_expenses) }
            showProgressBar(false)
        }
    }

    suspend fun addNewExpenseToThisMonth(groupId: String, name: String, amount: BigDecimal){
        withContext(dispatchersIO){
            showProgressBar(true)
            val roundedAmount = amount.setScale(2, BigDecimal.ROUND_HALF_UP)
            repository.updateThisMonthAndAddNewExpense(groupId, name, roundedAmount)?.let { error ->
                postMessage(error)
                showProgressBar(false)
            } ?: kotlin.run {
                getExpenses(groupId)
                getMembers(groupId)
            }
        }
    }

}