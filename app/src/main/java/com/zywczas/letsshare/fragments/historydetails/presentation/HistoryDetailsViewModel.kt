package com.zywczas.letsshare.fragments.historydetails.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.withBalance
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.historydetails.domain.HistoryDetailsRepository
import com.zywczas.letsshare.model.ExpenseDomain
import com.zywczas.letsshare.model.GroupMemberDomain
import com.zywczas.letsshare.model.GroupMonthDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryDetailsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: HistoryDetailsRepository
) : BaseViewModel() {

    private val _month = MutableLiveData<GroupMonthDomain>()
    private val month: LiveData<GroupMonthDomain> = _month

    private val _isMembersProgressBarVisible = MutableLiveData<Boolean>()
    val isMembersProgressBarVisible: LiveData<Boolean> = _isMembersProgressBarVisible

    val settledUpMessage: LiveData<Int> = Transformations.switchMap(month) { month ->
        liveData(dispatchersIO) {
            if (month.isSettledUp) { emit(R.string.settled_up_message) }
            else { emit(R.string.not_settled_up_message) }
        }
    }

    val monthlySum: LiveData<String> = Transformations.switchMap(month) { month ->
        liveData(dispatchersIO) {
            emit(month.totalExpenses.toString())
        }
    }

    val members: LiveData<List<GroupMemberDomain>> =
        Transformations.switchMap(month) { month ->
            liveData(dispatchersIO) {
                _isMembersProgressBarVisible.postValue(true)
                repository.getMembers(month.id)?.let { emit(it.withBalance(month.totalExpenses)) }
                    ?: postMonthError()
                _isMembersProgressBarVisible.postValue(false)
            }
        }

    private fun postMonthError() = postMessage(R.string.cant_get_month)

    val expenses: LiveData<List<ExpenseDomain>> = Transformations.switchMap(month) { month ->
        liveData(dispatchersIO) {
            showProgressBar(true)
            repository.getExpenses(month.id)?.let { emit(it) }
                ?: postMessage(R.string.cant_get_expenses)
            showProgressBar(false)
        }
    }

    fun getMonthDetails(month: GroupMonthDomain){
        viewModelScope.launch(dispatchersIO) {
            _month.postValue(month)
        }
    }

    fun settleUp(){
        viewModelScope.launch(dispatchersIO) {
            month.value?.let {
                repository.settleUpMonth(it.id)?.let { error -> postMessage(error) }
                    ?: getMonthDetails(GroupMonthDomain(it.id, it.totalExpenses, isSettledUp = true))
            } ?: postMonthError()
        }
    }

}