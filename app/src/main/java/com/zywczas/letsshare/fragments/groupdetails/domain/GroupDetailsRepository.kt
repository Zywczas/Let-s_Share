package com.zywczas.letsshare.fragments.groupdetails.domain

import com.zywczas.letsshare.model.*
import java.math.BigDecimal

interface GroupDetailsRepository {

    suspend fun getMonths(): List<GroupMonthDomain>?

    suspend fun getMembers(groupId: String): List<GroupMemberDomain>?

    suspend fun getMonth(): GroupMonthDomain?

    suspend fun getExpenses(groupId: String): List<ExpenseDomain>?

    suspend fun updateThisMonthAndAddNewExpense(groupId: String, name: String, amount: BigDecimal): Int?

}