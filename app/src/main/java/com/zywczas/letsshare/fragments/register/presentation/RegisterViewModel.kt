package com.zywczas.letsshare.fragments.register.presentation

import android.util.Patterns
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

    fun registerUser(name: String, email1: String,  email2: String, password1: String, password2: String) {
        viewModelScope.launch(dispatchersIO) {
            showProgressBar(true)
            repository.saveLastUsedEmail(email1)
            when {
                sessionManager.isNetworkAvailable().not() -> postMessage(R.string.connection_problem)
                areCredentialsValid(name, email1, email2, password1, password2) -> registerToFirebase(name, email1, password1)
            }
            showProgressBar(false)
        }
    }

    private suspend fun areCredentialsValid(name: String, email1: String,  email2: String, password1: String, password2: String): Boolean =
        if (name.isBlank() || email1.isBlank() || email2.isBlank() || password1.isBlank() || password2.isBlank()) {
            postMessage(R.string.name_email_password_not_blank)
            false
        } else if (password1.length < 6) {
            postMessage(R.string.password_needs_6letters)
            false
        } else if (email1 != email2){
            postMessage(R.string.different_emails)
            false
        } else if (password1 != password2){
            postMessage(R.string.different_passwords)
            false
        } else if (Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            when(repository.isEmailFreeToUse(email1)){
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
        } else {
            postMessage(R.string.invalid_email)
            false
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