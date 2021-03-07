package com.zywczas.letsshare.fragmentmain.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule
import com.zywczas.letsshare.di.modules.DispatchersModule.*
import com.zywczas.letsshare.fragmentmain.domain.MainRepository
import com.zywczas.letsshare.utils.SingleLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val mainRepository: MainRepository
): ViewModel(){

    init {
       //todo viewModelScope.launch(dispatchersIO){}
    }

    private suspend fun getFriends() {
        withContext(dispatchersIO){  }
    }

    suspend fun logout() {
        withContext(dispatchersIO){ mainRepository.logout() }
    }

    suspend fun addFriendByEmail(email: String){
        withContext(dispatchersIO){  }
    }

}

//todo dac sprawdzenie polaczenia