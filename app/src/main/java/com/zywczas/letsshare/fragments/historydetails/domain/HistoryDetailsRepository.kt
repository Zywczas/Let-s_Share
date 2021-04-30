package com.zywczas.letsshare.fragments.historydetails.domain

import com.zywczas.letsshare.models.ExpenseDomain
import com.zywczas.letsshare.models.GroupMemberDomain

interface HistoryDetailsRepository {

    suspend fun getMembers(monthId: String): List<GroupMemberDomain>?

    suspend fun getExpenses(monthId: String): List<ExpenseDomain>?

    suspend fun settleUpMonth(monthId: String): Int?

}