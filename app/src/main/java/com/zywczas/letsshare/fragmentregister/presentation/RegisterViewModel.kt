package com.zywczas.letsshare.fragmentregister.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragmentregister.domain.RegisterRepository
import com.zywczas.letsshare.utils.SingleLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _message = SingleLiveData<@StringRes Int>()
    val message: LiveData<Int> = _message

    private val _isRegisteredAndUserName = MutableLiveData<Pair<Boolean, String>>()
    val isRegisteredAndUserName: LiveData<Pair<Boolean, String>> = _isRegisteredAndUserName

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible

    suspend fun registerUser(name: String, email: String, password: String) {
        withContext(dispatchersIO) {
            _isProgressBarVisible.postValue(true)
            when { //todo sprawdzic czy to dobrze jest zamiast if'ow
                areCredentialsValid(name, email, password).not() -> {
                    _isProgressBarVisible.postValue(false)
                    return@withContext
                }
                sessionManager.isNetworkAvailable().not() -> {
                    _message.postValue(R.string.problem_connection)
                    _isProgressBarVisible.postValue(false)
                }
                else -> checkIfEmailIsFreeToUseAndRegisterToFirebase(name, email, password)
            }
        }
    }

    //todo trzeba tez sprawdzic czy email ma poprawna forme
    private fun areCredentialsValid(name: String, email: String, password: String): Boolean =
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _message.postValue(R.string.name_email_password_not_blank)
            false
        } else if (password.length < 6) {
            _message.postValue(R.string.password_needs_6letters)
            false
        } else {
            true
        }

    private suspend fun checkIfEmailIsFreeToUseAndRegisterToFirebase(name: String, email: String, password: String){
        registerRepository.isEmailFreeToUse(email){ isEmailFreeToUse ->
            if (isEmailFreeToUse) { viewModelScope.launch(dispatchersIO) { registerToFirebase(name, email, password) } }
            else {
                _message.postValue(R.string.email_already_exists)
                _isProgressBarVisible.postValue(false)
            }
        }
    }

    private suspend fun registerToFirebase(name: String, email: String, password: String){
        registerRepository.registerToFirebase(name, email, password) { isRegistered ->
            if (isRegistered) { viewModelScope.launch(dispatchersIO) { addUserToFirestore(name, email) } }
            else {
                _message.postValue(R.string.problem_registration)
                _isProgressBarVisible.postValue(false)
            }
        }
    }

    private suspend fun addUserToFirestore(name: String, email: String){
        registerRepository.addNewUserToFirestore(name, email){ isUserAdded ->
            if (isUserAdded) { viewModelScope.launch(dispatchersIO) { sendVerificationEmailAndLogout(name) } }
            else {
                _message.postValue(R.string.problem_registration)
                _isProgressBarVisible.postValue(false)
            }
        }
    }

    private suspend fun sendVerificationEmailAndLogout(name: String){
        registerRepository.sendEmailVerification { isEmailSent ->
            if (isEmailSent){
                viewModelScope.launch(dispatchersIO){
                    registerRepository.logoutFromFirebase()
                    _isProgressBarVisible.postValue(false)
                    _isRegisteredAndUserName.postValue(true to name)
                }
            }
            else {
                _message.postValue(R.string.problem_registration)
                _isProgressBarVisible.postValue(false)
            }
        }
    }

}