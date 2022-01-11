package com.zywczas.letsshare.utils.wrappers

import com.zywczas.letsshare.utils.LOCALE_POLAND
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateUtilImpl @Inject constructor() : DateUtil {

    override suspend fun presentDate(): Date = Date()

    override suspend fun presentMonthId(): String = SimpleDateFormat("yyyy-MM", LOCALE_POLAND).format(Date())

}