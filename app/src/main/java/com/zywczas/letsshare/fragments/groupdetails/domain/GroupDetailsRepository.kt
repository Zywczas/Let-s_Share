package com.zywczas.letsshare.fragments.groupdetails.domain

import com.zywczas.letsshare.models.ExpenseDomain
import com.zywczas.letsshare.models.GroupMemberDomain
import com.zywczas.letsshare.models.GroupMonthDomain
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface GroupDetailsRepository {

    suspend fun getLastMonth(): GroupMonthDomain?

    suspend fun listenToMonth(monthId: String): Flow<GroupMonthDomain>

    suspend fun getMembers(monthId: String): List<GroupMemberDomain>?

    suspend fun getExpenses(monthId: String): List<ExpenseDomain>?

    suspend fun createNewMonth(members: List<GroupMemberDomain>): Int?

    suspend fun addExpense(monthId: String, name: String, amount: BigDecimal): Int?

    suspend fun deleteExpense(monthId: String, expense: ExpenseDomain): Int?

}