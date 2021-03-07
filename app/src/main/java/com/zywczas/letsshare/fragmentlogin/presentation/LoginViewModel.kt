package com.zywczas.letsshare.fragmentlogin.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
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
): BaseViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    suspend fun login(email: String, password: String){
        withContext(dispatchersIO){
            if (sessionManager.isNetworkAvailable()){
                showProgressBar(true)
                loginRepository.loginToFirebase(email, password){ isSuccess, message ->
                    if (isSuccess) {
                        showProgressBar(false)
                        _isLoggedIn.postValue(true)
                    } else {
                        showProgressBar(false)
                        message?.let { postMessage(message) }
                    }
                }
            } else { postMessage(R.string.connection_problem) }
        }
    }


}