package com.zywczas.letsshare.extentions

import com.zywczas.letsshare.utils.wrappers.DateUtil
import java.text.SimpleDateFormat
import java.util.*

fun Date.monthId(): String = SimpleDateFormat("yyyy-MM", DateUtil.LOCALE_POLAND).format(this)

fun Date.dayFormat(): String = SimpleDateFormat("d.MM", DateUtil.LOCALE_POLAND).format(this)