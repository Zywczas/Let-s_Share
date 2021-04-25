package com.zywczas.letsshare.fragments.history.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.model.GroupMonth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher
) : BaseViewModel() {

    private val _months = MutableLiveData<List<GroupMonth>>()
    val months: LiveData<List<GroupMonth>> = _months

    fun getMonths(){
        viewModelScope.launch(dispatchersIO){

        }
    }

}