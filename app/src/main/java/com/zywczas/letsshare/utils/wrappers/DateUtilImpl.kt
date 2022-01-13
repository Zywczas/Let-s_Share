package com.zywczas.letsshare.utils.wrappers

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateUtilImpl @Inject constructor() : DateUtil {

    override suspend fun presentDate(): Date = Date()

    override suspend fun presentMonthId(): String = SimpleDateFormat("yyyy-MM", DateUtil.LOCALE_POLAND).format(Date())

}