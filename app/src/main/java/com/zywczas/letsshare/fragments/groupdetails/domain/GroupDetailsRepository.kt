package com.zywczas.letsshare.fragments.groupdetails.domain

import com.zywczas.letsshare.model.*
import java.math.BigDecimal

interface GroupDetailsRepository {

    suspend fun getLastMonth(): GroupMonthDomain?

    suspend fun getMembers(monthId: String): List<GroupMemberDomain>?

    suspend fun getExpenses(monthId: String): List<ExpenseDomain>?

    suspend fun createNewMonth(members: List<GroupMemberDomain>): Int?

    suspend fun updateThisMonthAndAddNewExpense(groupId: String, name: String, amount: BigDecimal): Int?

}