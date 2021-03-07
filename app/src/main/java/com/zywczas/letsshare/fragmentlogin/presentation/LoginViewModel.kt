package com.zywczas.letsshare.fragmentlogin.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragmentlogin.domain.LoginRepository
import com.zywczas.letsshare.utils.SingleLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val loginRepository: LoginRepository,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _message = SingleLiveData<@StringRes Int>()
    val message: LiveData<Int> = _message

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible

    suspend fun login(email: String, password: String){
        withContext(dispatchersIO){
            if (sessionManager.isNetworkAvailable()){
                _isProgressBarVisible.postValue(true)
                loginRepository.loginToFirebase(email, password){ isSuccess, message ->
                    if (isSuccess) {
                        _isProgressBarVisible.postValue(false)
                        _isLoggedIn.postValue(true)
                    } else {
                        _isProgressBarVisible.postValue(false)
                        message?.let { _message.postValue(message) }
                    }
                }
            } else { _message.postValue(R.string.connection_problem) }
        }
    }


}