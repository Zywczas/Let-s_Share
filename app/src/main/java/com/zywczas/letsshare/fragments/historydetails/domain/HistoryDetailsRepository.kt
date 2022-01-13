package com.zywczas.letsshare.fragments.historydetails.domain

import com.zywczas.letsshare.models.Expense
import com.zywczas.letsshare.models.GroupMember

interface HistoryDetailsRepository {

    suspend fun getMembers(monthId: String): List<GroupMember>?

    suspend fun getExpenses(monthId: String): List<Expense>?

    suspend fun settleUpMonth(monthId: String): Int?

}