package com.zywczas.letsshare.fragments.groupdetails.domain

import com.zywczas.letsshare.models.Expense
import com.zywczas.letsshare.models.GroupMember
import com.zywczas.letsshare.models.GroupMonthDomain
import com.zywczas.letsshare.models.local.UserLocal
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface GroupDetailsRepository {

    suspend fun getUser(): UserLocal

    suspend fun getLastMonth(): GroupMonthDomain?

    fun listenToMonth(monthId: String): Flow<GroupMonthDomain>

    suspend fun getMembers(monthId: String): List<GroupMember>?

    suspend fun getExpenses(monthId: String): List<Expense>?

    suspend fun createNewMonth(members: List<GroupMember>): Int?

    suspend fun addExpense(monthId: String, name: String, amount: BigDecimal): Int?

    suspend fun deleteExpense(monthId: String, expense: Expense): Int?

}