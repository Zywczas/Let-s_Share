package com.zywczas.letsshare.fragments.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.login.domain.LoginRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: LoginRepository,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    private val _lastUsedEmail = MutableLiveData<String>()
    val lastUsedEmail: LiveData<String> = _lastUsedEmail

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun getLastUsedEmail() {
        viewModelScope.launch(dispatchersIO) {
            _lastUsedEmail.postValue(repository.getLastUsedEmail())
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(dispatchersIO) {
            repository.saveLastUsedEmail(email)
            if (sessionManager.isNetworkAvailable()) {
                showProgressBar(true)
                loginToFirebase(email, password)
                showProgressBar(false)
            } else {
                postMessage(R.string.connection_problem)
            }
        }
    }

    private suspend fun loginToFirebase(email: String, password: String) {
        repository.loginToFirebase(email, password)?.let { error -> postMessage(error) }
            ?: kotlin.run {
                repository.saveUserLocally()?.let { error -> postMessage(error) }
                    ?: kotlin.run { //todo dac zapisywanie tokena w innym miejscu bo jak ktos jest juz zalogowany to ten fragment sie nie odpala
                        sessionManager.saveMessagingToken()
                        _isLoggedIn.postValue(true)
                    }
            }
    }

}