package com.zywczas.letsshare.fragments.groupdetails.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.withBalance
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.groupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.models.ExpenseDomain
import com.zywczas.letsshare.models.GroupMemberDomain
import com.zywczas.letsshare.models.GroupMonthDomain
import com.zywczas.letsshare.utils.monthId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class GroupDetailsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupDetailsRepository
) : BaseViewModel() {

    private val _currentMonth = MutableLiveData<GroupMonthDomain>()
    val currentMonth: LiveData<GroupMonthDomain> = _currentMonth

    private val _isMembersProgressBarVisible = MutableLiveData<Boolean>()
    val isMembersProgressBarVisible: LiveData<Boolean> = _isMembersProgressBarVisible

    val monthlySum: LiveData<String> = Transformations.switchMap(currentMonth) { month ->
        liveData(dispatchersIO) {
            emit(month.totalExpenses.toString())
        }
    }

    val members: LiveData<List<GroupMemberDomain>> =
        Transformations.switchMap(currentMonth) { month ->
            liveData(dispatchersIO) {
                _isMembersProgressBarVisible.postValue(true)
                repository.getMembers(month.id)?.let { emit(it.withBalance(month.totalExpenses)) }
                    ?: postMonthError()
                _isMembersProgressBarVisible.postValue(false)
            }
        }

    private fun postMonthError() = postMessage(R.string.cant_get_month)

    val expenses: LiveData<List<ExpenseDomain>> = Transformations.switchMap(currentMonth) { month ->
        liveData(dispatchersIO) {
            showProgressBar(true)
            repository.getExpenses(month.id)?.let { emit(it) }
                ?: postMessage(R.string.cant_get_expenses)
            showProgressBar(false)
        }
    }

    fun getMonthDetails() {
        viewModelScope.launch(dispatchersIO) {
            showProgressBar(true)
            repository.getLastMonth()?.let { lastMonth ->
                val hasNewCalendarMonthStarted = lastMonth.id != Date().monthId()
                if (hasNewCalendarMonthStarted) {
                    startNewMonth(lastMonth.id)
                } else {
                    _currentMonth.postValue(lastMonth)
                    listenToMonth(lastMonth.id)
                }
            } ?: postMonthError()
            showProgressBar(false)
        }
    }

    private suspend fun startNewMonth(lastMonthId: String) {
        showProgressBar(true)
        repository.getMembers(lastMonthId)?.let { members ->
            repository.createNewMonth(members)?.let { error -> postMessage(error) }
                ?: listenToMonth(Date().monthId())
        } ?: postMonthError()
        showProgressBar(false)
    }

    private suspend fun listenToMonth(monthId: String) {
        repository.listenToMonth(monthId)
            .buffer(Channel.CONFLATED)
            .catch { postMonthError() }
            .collect { _currentMonth.postValue(it) }
    }

    fun addExpense(name: String, amount: BigDecimal) {
        viewModelScope.launch(dispatchersIO) {
            currentMonth.value?.id?.let { monthId ->
                showProgressBar(true)
                val roundedAmount = amount.setScale(2, BigDecimal.ROUND_HALF_UP)
                repository.addExpense(monthId, name, roundedAmount)?.let { error ->
                    postMessage(error)
                    showProgressBar(false)
                }
            }
        }
    }

    fun deleteExpense(position: Int){
        viewModelScope.launch(dispatchersIO){
            showProgressBar(true)
            repository.delete(expenses.value!![position])?.let { error ->
                postMessage(error)
                showProgressBar(false)
            }
        }
    }

}
