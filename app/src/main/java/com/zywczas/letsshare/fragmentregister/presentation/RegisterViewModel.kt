package com.zywczas.letsshare.fragmentregister.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.utils.SingleLiveData
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val sessionManager: SessionManager
): ViewModel() {

    private val _message = SingleLiveData<@StringRes Int>()
    val message: LiveData<Int> = _message

    fun registerNewUser(email: String, password: String){
        //todo dodac verifikacje maila i hasla na conajmniej 6 znakow
        if (email.isBlank() || password.isBlank()){
            _message.postValue()
        }
    }

}