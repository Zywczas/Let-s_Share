package com.zywczas.letsshare.fragments.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.BaseViewModel
import com.zywczas.letsshare.fragments.settings.domain.SettingsRepository
import com.zywczas.letsshare.models.local.UserLocal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val repository: SettingsRepository
) : BaseViewModel() {

    private val _user = MutableLiveData<UserLocal>()
    val user: LiveData<UserLocal> = _user

    private val _isLoggedOut = MutableLiveData<Boolean>()
    val isLoggedOut: LiveData<Boolean> = _isLoggedOut

    fun getUserData() {
        viewModelScope.launch(dispatchersIO) {
            _user.postValue(repository.getUser())
        }
    }

    fun logout() {
        viewModelScope.launch(dispatchersIO){
            sessionManager.logout()
            _isLoggedOut.postValue(true)
        }
    }

}