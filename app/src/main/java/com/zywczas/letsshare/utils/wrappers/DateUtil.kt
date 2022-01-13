package com.zywczas.letsshare.utils.wrappers

import java.util.*

interface DateUtil {

    companion object {
        val LOCALE_POLAND: Locale = Locale("pl", "PL")
    }

    suspend fun presentDate(): Date

    suspend fun presentMonthId(): String

}