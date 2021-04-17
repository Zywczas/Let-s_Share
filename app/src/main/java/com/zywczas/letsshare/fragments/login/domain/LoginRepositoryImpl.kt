package com.zywczas.letsshare.fragments.login.domain

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.utils.COLLECTION_USERS
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firestoreRefs: FirestoreReferences,
    private val sharedPrefs: SharedPrefsWrapper
    ): LoginRepository {

    override suspend fun loginToFirebase(email: String, password: String, onSuccessAction: (User?, Int?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            if (firebaseAuth.currentUser!!.isEmailVerified){
                getUser(email, onSuccessAction)
            } else {
                firebaseAuth.signOut()
                onSuccessAction(null, R.string.verify_email)
            }
        }.addOnFailureListener { //todo dac crashlytics
            logD(it)
            onSuccessAction(null, R.string.login_problem)
        }
    }

    //todo dac await() w login, registration, main i welcome modulach
    suspend fun loginToFirebase2(email: String, password: String): AuthResult? =
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception){ //todo dac crashlytics
            logD(e)
            null
        }

    private fun getUser(email: String, onSuccessAction: (User?, Int?) -> Unit){
        firestore.collection(COLLECTION_USERS).document(email).get().addOnSuccessListener { userDocument ->
//        firestoreRefs.userRefs(email).get().addOnSuccessListener { userDocument -> //todo tak pozniej dac
            val user = userDocument.toObject<User>()
            user?.let {
                logD("zalogowany uzytkownik: $user")
                onSuccessAction(it, null)
            }
        }.addOnFailureListener {
            logD(it)
            onSuccessAction(null, R.string.login_problem)
        }
    }

    override suspend fun saveUserLocally(user: User) = sharedPrefs.saveUserLocally(user)

}