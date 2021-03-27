package com.zywczas.letsshare.fragments.register.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.dayFormat
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import java.util.*
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreRefs: FirestoreReferences
) : RegisterRepository {

    //todo to mi rzuca exception jak zly format maila, trzeba to poprawic na succeess i failure pewnie
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

     override suspend fun registerToFirebase(name: String, email: String, password: String, onSuccessAction: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            firebaseAuth.currentUser?.updateProfile(profileUpdate)?.addOnCompleteListener {
                onSuccessAction(true)
            }?.addOnFailureListener {
                logD(it)
                onSuccessAction(false)
            }
        }.addOnFailureListener {
            logD(it)
            onSuccessAction(false)
        }
    }

//todo to powinno dziac sie od razy przy rejestracji do firebase, a nie dopiero inicjowane w view modelu
    override suspend fun addNewUserToFirestore(name: String, email: String, onSuccessAction: (Boolean) -> Unit){
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null){
            firestoreRefs.userRefs(email)
                .set(User(userId, name, email, Date().dayFormat()))
                .addOnSuccessListener {
                    onSuccessAction(true)
                }.addOnFailureListener {
                    logD(it)
                    onSuccessAction(false)
                }
        } else { onSuccessAction(false) }
    }

    override suspend fun sendEmailVerification(onSuccessAction: (Boolean) -> Unit) {
        firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            onSuccessAction(true)
        }?.addOnFailureListener {
            logD(it)
            onSuccessAction(false)
        }
    }

    override suspend fun logoutFromFirebase() = firebaseAuth.signOut()

}