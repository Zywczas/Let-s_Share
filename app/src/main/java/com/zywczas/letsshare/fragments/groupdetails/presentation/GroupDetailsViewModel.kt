package com.zywczas.letsshare.fragments.groupdetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.groupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.model.ExpenseDomain
import com.zywczas.letsshare.model.GroupMemberDomain
import com.zywczas.letsshare.model.GroupMonthDomain
import com.zywczas.letsshare.utils.monthId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class GroupDetailsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupDetailsRepository
) : BaseViewModel() {

    private val _currentMonth = MutableLiveData<GroupMonthDomain>()
    private val currentMonth: LiveData<GroupMonthDomain> = _currentMonth

    val monthlySum: LiveData<String> = Transformations.switchMap(currentMonth){ month ->
        liveData(dispatchersIO){
            emit(month.totalExpenses.toString())
        }
    }

    private val _isMembersProgressBarVisible = MutableLiveData<Boolean>()
    val isMembersProgressBarVisible: LiveData<Boolean> = _isMembersProgressBarVisible

    val expenses: LiveData<List<ExpenseDomain>> = Transformations.switchMap(currentMonth){ month ->
        liveData(dispatchersIO){
            showProgressBar(true)
            repository.getExpenses(month.id)?.let { emit(it) } ?: postMessage(R.string.cant_get_expenses)
            showProgressBar(false)
        }
    }

    val members: LiveData<List<GroupMemberDomain>> = Transformations.switchMap(currentMonth){ month ->
        liveData(dispatchersIO){
            _isMembersProgressBarVisible.postValue(true)
            repository.getMembers(month.id)?.let { emit(it.withBalance(month.totalExpenses)) }
                ?: postMessage(R.string.cant_get_month)
            _isMembersProgressBarVisible.postValue(false)
        }
    }

    private fun List<GroupMemberDomain>.withBalance(groupTotalExpense: BigDecimal): List<GroupMemberDomain>{
        forEach { member ->
            val whatShouldPay = groupTotalExpense.multiply(member.share).divide(BigDecimal((100)))
            val balance = whatShouldPay.minus(member.expenses)
            if (balance > BigDecimal.ZERO) { member.owesOrOver = R.string.owes}
            else { member.owesOrOver = R.string.over }
            member.difference = balance.setScale(2, BigDecimal.ROUND_HALF_UP).abs()
        }
        return this
    }

    suspend fun getMonthDetails(){
        withContext(dispatchersIO){
            showProgressBar(true)
            repository.getLastMonth()?.let { month->
                if ( month.id != Date().monthId()) { startNewMonth(month.id) }
                else { _currentMonth.postValue(month) }
            } ?: postMessage(R.string.cant_get_month)
            showProgressBar(false)
        }
    }

    private suspend fun startNewMonth(lastMonthId: String){
        withContext(dispatchersIO){
            showProgressBar(true)
            repository.getMembers(lastMonthId)?.let { members ->
                repository.createNewMonth(members)?.let { error-> postMessage(error) }
                    ?: getMonthDetails()
            } ?: postMessage(R.string.cant_get_month)
            showProgressBar(false)
        }
    }

    suspend fun addExpense(name: String, amount: BigDecimal){
        withContext(dispatchersIO){
            currentMonth.value?.id?.let { monthId ->
                showProgressBar(true)
                val roundedAmount = amount.setScale(2, BigDecimal.ROUND_HALF_UP)
                repository.addExpense(monthId, name, roundedAmount)?.let { error ->
                    postMessage(error)
                    showProgressBar(false)
                } ?: getMonthDetails() //todo jak dam nasluchiwanie to to usunac
            } ?: postMessage(R.string.cant_get_month)
        }
    }

}