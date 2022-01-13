package com.zywczas.letsshare.fragments.history.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.BaseViewModel
import com.zywczas.letsshare.fragments.history.domain.HistoryRepository
import com.zywczas.letsshare.models.GroupMonth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: HistoryRepository
) : BaseViewModel() {

    private val _months = MutableLiveData<List<GroupMonth>>()
    val months: LiveData<List<GroupMonth>> = _months

    fun getMonths(){
        viewModelScope.launch(dispatchersIO){
            showProgressBar(true)
            repository.getPreviousMonths()?.let { _months.postValue(it) }
                ?: postMessage(R.string.cant_get_months_list)
            showProgressBar(false)
        }
    }

}