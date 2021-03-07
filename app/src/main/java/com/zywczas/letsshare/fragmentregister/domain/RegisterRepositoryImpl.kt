package com.zywczas.letsshare.fragmentregister.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.utils.COLLECTION_USERS
import com.zywczas.letsshare.utils.logD
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
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

    override suspend fun addNewUserToFirestore(name: String, email: String, onSuccessAction: (Boolean) -> Unit){
        val newUserRef = firestore.collection(COLLECTION_USERS).document()
        newUserRef.set(User(newUserRef.id, name, email)).addOnSuccessListener {
            onSuccessAction(true)
        }.addOnFailureListener {
            logD(it)
            onSuccessAction(false)
        }
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