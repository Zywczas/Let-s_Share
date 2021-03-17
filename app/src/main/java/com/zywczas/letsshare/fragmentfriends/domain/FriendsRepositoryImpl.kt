package com.zywczas.letsshare.fragmentfriends.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.utils.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendsRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sharedPrefs: SharedPrefsWrapper
) : FriendsRepository {
    //todo wrzucic w session manager
    override suspend fun logout() = firebaseAuth.signOut()

    override suspend fun getFriends(): List<Friend>? =
        try {
            val friends = firestore.collection(COLLECTION_USERS)
                .document(sharedPrefs.userEmail)
                .collection(COLLECTION_FRIENDS)
                .orderBy(FIELD_NAME, Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects<Friend>()
            friends.forEach { logD("pobrany znajomy: $it") }
            friends
        } catch (e: Exception) {
            logD(e)
            null
        }

    //todo dodac wszedzie pokazywanie progress bar
    override suspend fun addFriendByEmail(email: String, onFinishAction: (Int) -> Unit) {
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            val isFriendRegistered = task.result?.signInMethods?.size != null &&
                    task.result!!.signInMethods!!.size > 0
            if (isFriendRegistered) {
                firestore.collection(COLLECTION_USERS).document(email).get()
                    .addOnSuccessListener { firestoreUser ->
                        val user = firestoreUser.toObject<User>()
                        user?.let { addFriendToFirestoreCollection(it.toFriend(), onFinishAction) }
                    }.addOnFailureListener {
                    logD(it)
                    onFinishAction(R.string.cant_add_friend)
                }
            } else {
                onFinishAction(R.string.user_with_email_not_in_database)
            }
        }
    }

    private fun addFriendToFirestoreCollection(friend: Friend, onFinishAction: (Int) -> Unit) {
        firestore.collection(COLLECTION_USERS)
            .document(sharedPrefs.userEmail)
            .collection(COLLECTION_FRIENDS)
            .document(friend.email)
            .set(friend)
            .addOnSuccessListener {
                onFinishAction(R.string.friend_added)
            }.addOnFailureListener {
                logD(it)
                onFinishAction(R.string.cant_add_friend)
            }
    }

    private fun User.toFriend() = Friend(this.name, this.email)

//    private fun User.toDomain() = UserDomain(this.name, this.email)

}