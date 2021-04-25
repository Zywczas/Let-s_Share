package com.zywczas.letsshare.fragments.history.domain

import com.zywczas.letsshare.model.GroupMonthDomain

interface HistoryRepository {

    suspend fun getPreviousMonths(): List<GroupMonthDomain>?

}