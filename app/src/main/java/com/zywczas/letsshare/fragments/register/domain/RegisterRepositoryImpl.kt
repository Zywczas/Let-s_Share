package com.zywczas.letsshare.fragments.register.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.zywczas.letsshare.R
import com.zywczas.letsshare.extentions.logD
import com.zywczas.letsshare.models.firestore.UserFire
import com.zywczas.letsshare.utils.wrappers.CrashlyticsWrapper
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import com.zywczas.letsshare.utils.wrappers.SharedPrefsWrapper
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreRefs: FirestoreReferences,
    private val sharedPrefs: SharedPrefsWrapper,
    private val crashlyticsWrapper: CrashlyticsWrapper
) : RegisterRepository {

    override suspend fun saveLastUsedEmail(email: String) {
        sharedPrefs.lastUsedEmail = email
    }

    override suspend fun isEmailFreeToUse(email: String): Boolean? =
        try {
            val singInMethods = firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods
            val isEmailAlreadyTaken = singInMethods?.size != null && singInMethods.size > 0
            isEmailAlreadyTaken.not()
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun registerToFirebase(name: String, email: String, password: String): Int? =
        try {
            val registration = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (registration.user != null){
                val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()
                firebaseAuth.currentUser!!.updateProfile(profileUpdate).await()
                null
            } else {
                R.string.problem_registration
            }
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.problem_registration
        }

    override suspend fun addUserToFirestore(name: String, email: String): Int? =
        try {
            val authId = firebaseAuth.currentUser!!.uid
            val user = UserFire(id = authId, name = name, email = email)
            firestoreRefs.userRefs(authId).set(user).await()
            null
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.problem_registration
        }

    override suspend fun sendVerificationEmail(): Int? =
        try {
            firebaseAuth.currentUser!!.sendEmailVerification().await()
            null
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.problem_registration
        }

    override suspend fun logoutFromFirebase() = firebaseAuth.signOut()

}