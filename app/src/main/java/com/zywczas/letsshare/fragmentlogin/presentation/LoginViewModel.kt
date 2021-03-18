package com.zywczas.letsshare.fragmentlogin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragmentlogin.domain.LoginRepository
import com.zywczas.letsshare.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val repository: LoginRepository,
    private val sessionManager: SessionManager
): BaseViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    suspend fun login(email: String, password: String){
        withContext(dispatchersIO){
            if (sessionManager.isNetworkAvailable()){
                loginToFirebase(email, password)
            } else { postMessage(R.string.connection_problem) }
        }
    }

    private suspend fun loginToFirebase(email: String, password: String){
        showProgressBar(true)
        repository.loginToFirebase(email, password){ user, message ->
            viewModelScope.launch(dispatchersIO) {
                showProgressBar(false)
                if (user != null) {
                    saveUserLocally(user)
                    _isLoggedIn.postValue(true)
                } else {
                    message?.let { postMessage(message) }
                }
            }
        }
    }

    private suspend fun saveUserLocally(user: User) = repository.saveUserLocally(user)

}