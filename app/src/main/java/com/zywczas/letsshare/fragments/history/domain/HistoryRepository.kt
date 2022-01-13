package com.zywczas.letsshare.fragments.history.domain

import com.zywczas.letsshare.models.GroupMonth

interface HistoryRepository {

    suspend fun getPreviousMonths(): List<GroupMonth>?

}