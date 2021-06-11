package com.zywczas.letsshare.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object LiveDataTestUtil {

    fun <T> getValue(livedata: LiveData<T>): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object: Observer<T> {
            override fun onChanged(t: T) {
                data = t
                latch.countDown()
                livedata.removeObserver(this)
            }
        }
        livedata.observeForever(observer)
        if (!latch.await(2, TimeUnit.SECONDS)) {
            livedata.removeObserver(observer)
            throw TimeoutException("LiveData value was never set.")
        }
        @Suppress("UNCHECKED_CAST")
        return data as T
    }

}