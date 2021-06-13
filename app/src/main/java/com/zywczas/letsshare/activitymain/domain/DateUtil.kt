package com.zywczas.letsshare.activitymain.domain

import java.util.*

interface DateUtil {

    suspend fun presentDate(): Date

    suspend fun presentMonthId(): String

}