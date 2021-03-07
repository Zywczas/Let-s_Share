package com.zywczas.letsshare.fragmentregister.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragmentregister.domain.RegisterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val registerRepository: RegisterRepository
) : BaseViewModel() {

    private val _isRegisteredAndUserName = MutableLiveData<Pair<Boolean, String>>()
    val isRegisteredAndUserName: LiveData<Pair<Boolean, String>> = _isRegisteredAndUserName

    suspend fun registerUser(name: String, email: String, password: String) {
        withContext(dispatchersIO) {
            showProgressBar(true)
            when { //todo sprawdzic czy to dobrze jest zamiast if'ow
                areCredentialsValid(name, email, password).not() -> {
                    showProgressBar(false)
                    return@withContext
                }
                sessionManager.isNetworkAvailable().not() -> {
                    postMessage(R.string.connection_problem)
                    showProgressBar(false)
                }
                else -> checkIfEmailIsFreeToUseAndRegisterToFirebase(name, email, password)
            }
        }
    }

    //todo trzeba tez sprawdzic czy email ma poprawna forme
    private fun areCredentialsValid(name: String, email: String, password: String): Boolean =
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            postMessage(R.string.name_email_password_not_blank)
            false
        } else if (password.length < 6) {
            postMessage(R.string.password_needs_6letters)
            false
        } else {
            true
        }

    private suspend fun checkIfEmailIsFreeToUseAndRegisterToFirebase(name: String, email: String, password: String){
        registerRepository.isEmailFreeToUse(email){ isEmailFreeToUse ->
            if (isEmailFreeToUse) { viewModelScope.launch(dispatchersIO) { registerToFirebase(name, email, password) } }
            else {
                postMessage(R.string.email_already_exists)
                showProgressBar(false)
            }
        }
    }

    private suspend fun registerToFirebase(name: String, email: String, password: String){
        registerRepository.registerToFirebase(name, email, password) { isRegistered ->
            if (isRegistered) { viewModelScope.launch(dispatchersIO) { addUserToFirestore(name, email) } }
            else {
                postMessage(R.string.problem_registration)
                showProgressBar(false)
            }
        }
    }

    private suspend fun addUserToFirestore(name: String, email: String){
        registerRepository.addNewUserToFirestore(name, email){ isUserAdded ->
            if (isUserAdded) { viewModelScope.launch(dispatchersIO) { sendVerificationEmailAndLogout(name) } }
            else {
                postMessage(R.string.problem_registration)
                showProgressBar(false)
            }
        }
    }

    private suspend fun sendVerificationEmailAndLogout(name: String){
        registerRepository.sendEmailVerification { isEmailSent ->
            if (isEmailSent){
                viewModelScope.launch(dispatchersIO){
                    registerRepository.logoutFromFirebase()
                    showProgressBar(false)
                    _isRegisteredAndUserName.postValue(true to name)
                }
            }
            else {
                postMessage(R.string.problem_registration)
                showProgressBar(false)
            }
        }
    }

}