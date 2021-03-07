package com.zywczas.letsshare.fragmentmain.presentation

import androidx.lifecycle.ViewModel
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragmentmain.domain.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val mainRepository: MainRepository
): BaseViewModel(){

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
        withContext(dispatchersIO){
            if (sessionManager.isNetworkAvailable()) { mainRepository. }
            else { postMessage(R.string.connection_problem) }
        }
    }

}