package com.zywczas.letsshare.fragments.login.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObject
import com.zywczas.letsshare.R
import com.zywczas.letsshare.db.UserDao
import com.zywczas.letsshare.extentions.logD
import com.zywczas.letsshare.models.firestore.UserFire
import com.zywczas.letsshare.models.local.UserLocal
import com.zywczas.letsshare.utils.wrappers.CrashlyticsWrapper
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import com.zywczas.letsshare.utils.wrappers.SharedPrefsWrapper
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreRefs: FirestoreReferences,
    private val sharedPrefs: SharedPrefsWrapper,
    private val crashlytics: CrashlyticsWrapper,
    private val userDao: UserDao
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
            val user = firestoreRefs.userRefs(userId).get().await().toObject<UserFire>()!!.toLocal()
            userDao.clearTable()
            userDao.insert(user)
            null
        } catch (e: Exception) {
            crashlytics.sendExceptionToFirebase(e)
            logD(e)
            R.string.login_problem
        }

    private fun UserFire.toLocal() = UserLocal(
        id =id,
        name = name,
        email = email,
        groupsIds = groupsIds,
        messagingToken = messagingToken
    )

}