package com.zywczas.letsshare.fragmentlogin.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.utils.COLLECTION_USERS
import com.zywczas.letsshare.utils.logD
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
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
        }.addOnFailureListener {
            logD(it)
            onSuccessAction(null, R.string.login_problem)
        }
    }

    private fun getUser(email: String, onSuccessAction: (User?, Int?) -> Unit){
        firestore.collection(COLLECTION_USERS).document(email).get().addOnSuccessListener { userDocument ->
            val user = userDocument.toObject<User>()
            user?.let { onSuccessAction(it, null) }
        }.addOnFailureListener {
            logD(it)
            onSuccessAction(null, R.string.login_problem)
        }
    }

    override suspend fun saveUserLocally(user: User) = sharedPrefs.saveUserLocally(user)

}