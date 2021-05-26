package com.zywczas.letsshare.extentions

import com.zywczas.letsshare.utils.LOCALE_POLAND
import java.text.SimpleDateFormat
import java.util.*

fun Date.monthId(): String = SimpleDateFormat("yyyy-MM", LOCALE_POLAND).format(this)

fun Date.dayFormat(): String = SimpleDateFormat("d.MM", LOCALE_POLAND).format(this)