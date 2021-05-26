package com.zywczas.letsshare.extentions

import android.util.Log
import com.zywczas.letsshare.utils.LOCALE_POLAND
import java.util.*

fun Any.logD(e: Exception) = logD(e.message)

fun Any.logD(msg: String?) = Log.d("LetsShareTag in ${this.javaClass.simpleName}", "$msg")

fun dateInPoland(): Date = Calendar.getInstance(LOCALE_POLAND).time
