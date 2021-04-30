package com.zywczas.letsshare.fragments.historydetails.domain

import com.zywczas.letsshare.model.ExpenseDomain
import com.zywczas.letsshare.model.GroupMemberDomain

interface HistoryDetailsRepository {

    suspend fun getMembers(monthId: String): List<GroupMemberDomain>?

    suspend fun getExpenses(monthId: String): List<ExpenseDomain>?

    suspend fun settleUpMonth(monthId: String): Int?

}