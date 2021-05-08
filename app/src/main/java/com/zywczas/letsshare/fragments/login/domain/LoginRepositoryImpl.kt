package com.zywczas.letsshare.fragments.login.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.models.User
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreRefs: FirestoreReferences,
    private val sharedPrefs: SharedPrefsWrapper,
    private val crashlytics: CrashlyticsWrapper
): LoginRepository {

    override suspend fun getLastUsedEmail(): String = sharedPrefs.lastUsedEmail

    override suspend fun saveLastUsedEmail(email: String) {
        sharedPrefs.lastUsedEmail = email
    }

    override suspend fun loginToFirebase(email: String, password: String): Int? =
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result.user?.isEmailVerified == true){ null }
            else {
                firebaseAuth.signOut()
                R.string.verify_email
            }
        } catch (e: Exception){
            crashlytics.sendExceptionToFirebase(e)
            logD(e)
            R.string.login_problem
        }

    override suspend fun saveUserLocally(): Int? =
        try {
            val userId = firebaseAuth.currentUser?.uid!!
            val user = firestoreRefs.userRefs(userId).get().await().toObject<User>()!!
            sharedPrefs.saveUserLocally(user)
            null
        } catch (e: Exception) {
            crashlytics.sendExceptionToFirebase(e)
            logD(e)
            R.string.login_problem
        }

}