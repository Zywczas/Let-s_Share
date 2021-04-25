package com.zywczas.letsshare.fragments.register.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.register.domain.RegisterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val repository: RegisterRepository
) : BaseViewModel() {

    private val _isRegisteredAndUserName = MutableLiveData<Pair<Boolean, String>>()
    val isRegisteredAndUserName: LiveData<Pair<Boolean, String>> = _isRegisteredAndUserName

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch(dispatchersIO) {
            showProgressBar(true)
            repository.saveLastUsedEmail(email)
            when {
                sessionManager.isNetworkAvailable().not() -> postMessage(R.string.connection_problem)
                areCredentialsValid(name, email, password) -> registerToFirebase(name, email, password)
            }
            showProgressBar(false)
        }
    }

    private suspend fun areCredentialsValid(name: String, email: String, password: String): Boolean =
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            postMessage(R.string.name_email_password_not_blank)
            false
        } else if (password.length < 6) {
            postMessage(R.string.password_needs_6letters)
            false
        } else {
            when(repository.isEmailFreeToUse(email)){
                null -> {
                    postMessage(R.string.something_wrong)
                    false
                }
                false -> {
                    postMessage(R.string.email_already_exists)
                    false
                }
                true -> true
            }
        }

    private suspend fun registerToFirebase(name: String, email: String, password: String) {
        repository.registerToFirebase(name, email, password)?.let { error -> postMessage(error) }
            ?: kotlin.run {
                repository.addUserToFirestore(name, email)?.let { error -> postMessage(error) }
                repository.sendVerificationEmail()?.let { error -> postMessage(error) }
                repository.logoutFromFirebase()
                _isRegisteredAndUserName.postValue(true to name)
            }
    }

}