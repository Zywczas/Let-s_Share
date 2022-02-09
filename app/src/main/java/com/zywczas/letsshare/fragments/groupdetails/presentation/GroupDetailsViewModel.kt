package com.zywczas.letsshare.fragments.groupdetails.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.BaseViewModel
import com.zywczas.letsshare.fragments.groupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.models.Expense
import com.zywczas.letsshare.models.ExpenseNotification
import com.zywczas.letsshare.models.GroupMember
import com.zywczas.letsshare.models.GroupMonth
import com.zywczas.letsshare.utils.wrappers.DateUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class GroupDetailsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupDetailsRepository,
    private val sessionManager: SessionManager,
    private val dateUtil: DateUtil
) : BaseViewModel() {

    companion object {
        fun List<GroupMember>.withBalance(groupTotalExpense: BigDecimal): List<GroupMember> {
            forEach { member ->
                val whatShouldPay = groupTotalExpense.multiply(member.share).divide(BigDecimal((100)))
                val balance = whatShouldPay.minus(member.expenses)
                if (balance > BigDecimal.ZERO) {
                    member.owesOrOver = R.string.owes
                } else {
                    member.owesOrOver = R.string.over
                }
                member.difference = balance.setScale(2, BigDecimal.ROUND_HALF_UP).abs()
            }
            return this
        }
    }

    private val _currentMonth = MutableLiveData<GroupMonth>()
    val currentMonth: LiveData<GroupMonth> = _currentMonth

    private val _isMembersProgressBarVisible = MutableLiveData<Boolean>()
    val isMembersProgressBarVisible: LiveData<Boolean> = _isMembersProgressBarVisible

    val monthlySum: LiveData<String> = Transformations.switchMap(currentMonth) { month ->
        liveData(dispatchersIO) {
            emit(month.totalExpenses.toString())
        }
    }

    val members: LiveData<List<GroupMember>> =
        Transformations.switchMap(currentMonth) { month ->
            liveData(dispatchersIO) {
                _isMembersProgressBarVisible.postValue(true)
                repository.getMembers(month.id)?.let { emit(it.withBalance(month.totalExpenses)) }
                    ?: postMonthError()
                _isMembersProgressBarVisible.postValue(false)
            }
        }

    private fun postMonthError() = postMessage(R.string.cant_get_month)

    val expenses: LiveData<List<Expense>> = Transformations.switchMap(currentMonth) { month ->
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
                val hasNewCalendarMonthStarted = lastMonth.id != dateUtil.presentMonthId()
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
                ?: listenToMonth(dateUtil.presentMonthId())
        } ?: postMonthError()
        showProgressBar(false)
    }

    private suspend fun listenToMonth(monthId: String) {
        repository.listenToMonth(monthId)
            .buffer(Channel.CONFLATED)
            .catch { postMonthError() }
            .collect { _currentMonth.postValue(it) }
    }

    fun addExpense(name: String, amount: BigDecimal, groupName: String) {
        viewModelScope.launch(dispatchersIO) {
            currentMonth.value?.id?.let { monthId ->
                val roundedAmount = amount.setScale(2, BigDecimal.ROUND_HALF_UP)
                if (roundedAmount == BigDecimal("0.00")){
                    postMessage(R.string.expense_too_small)
                } else {
                    showProgressBar(true)
                    repository.addExpense(monthId, name, roundedAmount)?.let { error ->
                        postMessage(error)
                        showProgressBar(false)
                    } ?: sendNotification(groupName)
                }
            }
        }
    }

    private suspend fun sendNotification(groupName: String) {
        val user = repository.getUser()
        members.value.takeIf { it.isNullOrEmpty().not() }
            ?.filterNot { it.id == user.id }
            ?.map { it.id }
            ?.let { ids ->
                val notification = ExpenseNotification(
                    ownerName = user.name,
                    groupName = groupName,
                    receiversIds = ids
                )
                sessionManager.sendNotification(notification)
        }
    }

    fun deleteExpense(position: Int) {
        viewModelScope.launch(dispatchersIO){
            showProgressBar(true)
            repository.deleteExpense(currentMonth.value!!.id, expenses.value!![position])?.let { error ->
                postMessage(error)
                showProgressBar(false)
            }
        }
    }

}
