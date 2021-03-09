package com.zywczas.letsshare.fragmentwelcome.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.utils.SingleLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager
): ViewModel(), LifecycleObserver {

    private val _goToLoginFragment = SingleLiveData<Boolean>()
    val goToLoginFragment: LiveData<Boolean> = _goToLoginFragment

    private val _goToMainFragment = SingleLiveData<Boolean>()
    val goToMainFragment: LiveData<Boolean> = _goToMainFragment

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume(){
        viewModelScope.launch(dispatchersIO) {
            sessionManager.delayCoroutine(1000L) //just to present Welcome Screen to user
            chooseFragmentToGoNext()
        }
    }

    private suspend fun chooseFragmentToGoNext(){
        if (sessionManager.isUserLoggedIn()){ _goToMainFragment.postValue(true) }
        else { _goToLoginFragment.postValue(true) }
    }

}