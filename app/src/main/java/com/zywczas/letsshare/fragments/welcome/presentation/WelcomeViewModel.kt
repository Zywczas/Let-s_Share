package com.zywczas.letsshare.fragments.welcome.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.di.modules.UtilsModule.WelcomeScreenDelay
import com.zywczas.letsshare.utils.SingleLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    @WelcomeScreenDelay private val period: Long
): ViewModel() {

    private val _goToLoginFragment = SingleLiveData<Boolean>()
    val goToLoginFragment: LiveData<Boolean> = _goToLoginFragment

    private val _goToFriendsFragment = SingleLiveData<Boolean>()
    val goToFriendsFragment: LiveData<Boolean> = _goToFriendsFragment

    fun chooseNextDestination(){
        viewModelScope.launch(dispatchersIO) {
            presentLogoToUser()
            if (sessionManager.isUserLoggedIn()){
                sessionManager.saveMessagingToken()
                _goToFriendsFragment.postValue(true)
            } else {
                _goToLoginFragment.postValue(true)
            }
        }
    }

    private suspend fun presentLogoToUser() = sessionManager.delayCoroutine(period) //todo tu chyba moze byc samo delay, chyba nie potrzebuje tego w managerze

}