package com.zywczas.letsshare.activitymain.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.letsshare.utils.SingleLiveData

abstract class BaseViewModel: ViewModel() {

    private val _message = SingleLiveData<@StringRes Int>()
    val message: LiveData<Int> = _message

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible

    fun postMessage(@StringRes msg: Int){
        _message.postValue(msg)
    }

    fun showProgressBar(show: Boolean){
        _isProgressBarVisible.postValue(show)
    }

}