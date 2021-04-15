package com.zywczas.letsshare.fragments.groupdetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.groupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class GroupDetailsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: GroupDetailsRepository
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            val cos = repository.getMonths()
            val s = cos
        }
    }

    private val _members = MutableLiveData<List<GroupMemberDomain>>()
    val members: LiveData<List<GroupMemberDomain>> = _members

    private val _expenses = MutableLiveData<List<ExpenseDomain>>()
    val expenses: LiveData<List<ExpenseDomain>> = _expenses

    suspend fun getMembers(groupId: String) {
//        withContext(dispatchersIO){ //todo da gdzie indziej tak samo :) - czyli ?.let i run
//            showProgressBar(true)
//            repository.getMembers(groupId)?.let { members ->
//                repository.getMonth()?.let { month ->
//                    _members.postValue(updateExpenseBalance(members, month))
//                } ?: kotlin.run { postMessage(R.string.open_new_month) }
//            } ?: kotlin.run { postMessage(R.string.cant_get_group_members) }
//            showProgressBar(false)
//        }
    }

    //todo jezeli mamy nowy miesiac to nie da sie policzyc roznic
    //todo jak nei ma miesiaca o okreslonym id to rzuca exception
    //todo czyli trzeba pobierac liste wszystkich miesiecy i wtedy wystwietlac ostatni

    //chyba trzeba zrobic tak ze trzymam 6 miesiace max i najpierw pobieram miesiace, patrze ktory jest ostatni (dac date),
    //wtedy wyswietlam ostatni, a nowy mozna rozpoczac dopiero jak sie zamknie poprzedni
    //najpierw pobieram miesiac, potem uzytkownikow dla niego i wydatki, wiec chyba uzytkownikow trzeba trzymac w kazdym miesiacu osobno, skoro i tak musze mape tam wstawiac dla historii to bez sensu

    private fun updateExpenseBalance(members: List<GroupMemberDomain>, month: GroupMonthDomain): List<GroupMemberDomain>{
//        members.forEach { member ->
//            val balance = month.total_expenses
//        }
        return members
    }

    suspend fun getExpenses(groupId: String){
//        withContext(dispatchersIO){
//            showProgressBar(true)
//            repository.getExpenses(groupId)?.let { _expenses.postValue(it) }
//                ?: kotlin.run { postMessage(R.string.cant_get_expenses) }
//            showProgressBar(false)
//        }
    }

    //todo jezeli jest nowy miesiac to dac reset przy wysylaniu nowego wydatku, nie wysle nowego zanim nie zamknie miesiaca

    suspend fun addNewExpenseToThisMonth(groupId: String, name: String, amount: BigDecimal){
//        withContext(dispatchersIO){
//            showProgressBar(true)
//            val roundedAmount = amount.setScale(2, BigDecimal.ROUND_HALF_UP)
//            repository.updateThisMonthAndAddNewExpense(groupId, name, roundedAmount)?.let { error ->
//                postMessage(error)
//                showProgressBar(false)
//            } ?: kotlin.run {
//                getExpenses(groupId)
//                getMembers(groupId)
//            }
//        }
    }

}