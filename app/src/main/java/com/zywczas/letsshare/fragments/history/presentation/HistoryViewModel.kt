package com.zywczas.letsshare.fragments.history.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.fragments.history.domain.HistoryRepository
import com.zywczas.letsshare.model.GroupMonth
import com.zywczas.letsshare.model.GroupMonthDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: HistoryRepository
) : BaseViewModel() {

    private val _months = MutableLiveData<List<GroupMonthDomain>>()
    val months: LiveData<List<GroupMonthDomain>> = _months

    fun getMonths(){
        viewModelScope.launch(dispatchersIO){
            repository.getPreviousMonths()?.let { _months.postValue(it) }
                ?: postMessage(R.string.cant_get_months_list)
        }
    }

}