package com.zywczas.letsshare.fragmentlogin.domain

import com.google.firebase.auth.FirebaseAuth
import com.zywczas.letsshare.R
import com.zywczas.letsshare.utils.logD
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth): LoginRepository {

    override suspend fun loginToFirebase(email: String, password: String, onSuccessAction: (Boolean, Int?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            if (firebaseAuth.currentUser!!.isEmailVerified){ onSuccessAction(true, null) }
            else {
                firebaseAuth.signOut()
                onSuccessAction(false, R.string.verify_email)
            }
        }.addOnFailureListener {
            logD(it)
            onSuccessAction(false, R.string.login_problem)
        }
    }

}