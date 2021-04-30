package com.zywczas.letsshare.fragments.history.domain

import com.zywczas.letsshare.models.GroupMonthDomain

interface HistoryRepository {

    suspend fun getPreviousMonths(): List<GroupMonthDomain>?

}