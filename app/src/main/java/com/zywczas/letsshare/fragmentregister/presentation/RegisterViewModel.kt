package com.zywczas.letsshare.fragmentregister.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragmentregister.domain.RegisterRepository
import com.zywczas.letsshare.utils.SingleLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _message = SingleLiveData<@StringRes Int>()
    val message: LiveData<Int> = _message

    private var email =""
    private var password = ""
    private var onRegistrationSuccessAction: () -> Unit = {}

    //todo dodac powitanie przy rejestrowaniu sie, podawac imie od razu przy tworzeniu uzytkownika
    @Suppress("CascadeIf")
    suspend fun registerNewUser(email: String, password: String, onRegistrationSuccessAction: () -> Unit) {
        withContext(dispatchersIO) {
            this@RegisterViewModel.email = email
            this@RegisterViewModel.password = password
            this@RegisterViewModel.onRegistrationSuccessAction = onRegistrationSuccessAction
            if (areCredentialsValid().not()) {
                return@withContext
            } else if (sessionManager.isNetworkAvailable().not()) {
                _message.postValue(R.string.problem_connection)
            } else {
                checkIfEmailIsFreeToUseAndRegisterToFirebase()
            }
        }
    }

    //todo trzeba tez sprawdzic czy email ma poprawna forme
    private fun areCredentialsValid(): Boolean =
        if (email.isBlank() || password.isBlank()) {
            _message.postValue(R.string.email_and_password_not_blank)
            false
        } else if (password.length < 6) {
            _message.postValue(R.string.password_needs_6letters)
            false
        } else {
            true
        }

    private suspend fun checkIfEmailIsFreeToUseAndRegisterToFirebase(){
        registerRepository.isEmailFreeToUse(email){ isEmailFreeToUse ->
            if (isEmailFreeToUse) {
                registerToFirebase()
            } else {
                _message.postValue(R.string.email_already_exists)
            }
        }
    }

    private fun registerToFirebase(){
        registerRepository.registerToFirebase(email, password) { isRegistered ->
            if (isRegistered) { onRegistrationSuccessAction.invoke() }
            else { _message.postValue(R.string.problem_registration) }
        }
    }

}