package com.zywczas.letsshare.fragmentregister.domain

import com.google.firebase.auth.FirebaseAuth
import com.zywczas.letsshare.utils.logD
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : RegisterRepository {

    override suspend fun isEmailFreeToUse(email: String, onIsEmailFreeToUseAction: (Boolean) -> Unit) {
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
            val isEmailAlreadyTaken = it.result?.signInMethods?.size != null && it.result!!.signInMethods!!.size > 0
            if (isEmailAlreadyTaken.not()) {
                logD("email '$email' jeszcze nie istnieje w bazie")
                onIsEmailFreeToUseAction(true)
            } else {
                logD("email '$email' juz zostal uzyty")
                onIsEmailFreeToUseAction(false)
            }
        }
    }

    //jak ktos nowy sie rejestruje to dobrze jest go od razu dodac do bazy danych Users

    // auth.fetchSignInMethodsForEmail("email") - jezeli zwraca jakies metody tzn ze taki email istnieje

    //todo  sendVerificationEmailMOjaFun()
    //todo ta metoda autoamtycznie loguje uzytkownika jesli success, wiec ponizej jest wylogowanie
//                firebaseAuth.signOut()
//                logD("wylogowano")

     override fun registerToFirebase(email: String, password: String, onSuccessAction: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            onSuccessAction(true)
        }.addOnFailureListener {
            logD(it)
            onSuccessAction(false)
        }
    }

}