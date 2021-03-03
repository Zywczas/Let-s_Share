package com.zywczas.letsshare.fragmentregister.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.utils.SingleLiveData
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _message = SingleLiveData<@StringRes Int>()
    val message: LiveData<Int> = _message

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered

//    private val firebaseAuth = Firebase.auth

    suspend fun verifyCredentialsAndRegisterNewUser(email: String, password: String) {
        withContext(dispatchersIO) {
            //jak ktos nowy sie rejestruje to dobrze jest go od razu dodac do bazy danych Users
            //najpier sprawdzic czy taki mail juz nie istnieje w bazie
            // auth.fetchSignInMethodsForEmail("email") - jezeli zwraca jakies metody tzn ze taki email istnieje
            //trzeba tez sprawdzic czy email ma poprawna forme i czy haslo conajmniej 6 znakow
            //ta metoda autoamtycznie loguje uzytkownika jesli success, wiec ponizej jest wylogowanie

            //todo dodac powitanie przy rejestrowaniu sie, podawac imie od razu przy tworzeniu uzytkownika
            if (email.isBlank() || password.isBlank()) {
                _message.postValue(R.string.email_and_password_not_blank)
            } else if (password.length < 6) {
                _message.postValue(R.string.password_needs_6letters)
            } else if (sessionManager.isNetworkAvailable().not()){
                _message.postValue(R.string.problem_connection)
            } else {
                registerUserAndSendVerificationEmail(email, password)
            }
        }
    }

    private fun registerUserAndSendVerificationEmail(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            logD("utworzono nowego uzytkownika")
            logD("nowy uzytkownik: ${it.user}")
            //todo  sendVerificationEmailMOjaFun()
            //od razu wypisujemy, bo chcemy zwerifikowac email najpierw
//                firebaseAuth.signOut()
//                logD("wylogowano")
            _message.postValue(R.string.user_registered)
            _isRegistered.postValue(true)
        }.addOnFailureListener {
            logD(it)
            _message.postValue(R.string.problem_registration)
        }
    }

}