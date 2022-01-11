package com.zywczas.letsshare.utils.wrappers

import java.util.*

interface DateUtil {

    suspend fun presentDate(): Date

    suspend fun presentMonthId(): String

}