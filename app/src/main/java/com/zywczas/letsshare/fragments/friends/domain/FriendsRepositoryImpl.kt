package com.zywczas.letsshare.fragments.friends.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.model.db.FriendsDao
import com.zywczas.letsshare.utils.COLLECTION_FRIENDS
import com.zywczas.letsshare.utils.COLLECTION_USERS
import com.zywczas.letsshare.utils.FIELD_NAME
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendsRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firestoreRefs: FirestoreReferences,
    private val sharedPrefs: SharedPrefsWrapper,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    private val friendsDao: FriendsDao
) : FriendsRepository {

    //todo wrzucic w session manager
    override suspend fun logout() = firebaseAuth.signOut()

    private val userId = sharedPrefs.userId

    override suspend fun getFriends(): List<Friend>? =
        try {
            val friends = firestoreRefs.collectionFriends(userId)
                .orderBy(firestoreRefs.nameField, Query.Direction.ASCENDING)
                .get().await()
                .toObjects<Friend>()        //todo pozniej to poprawic, dac najpierw pobieranie z bazy, potem z firestore, i wtedy update, albo dac swipe to refresh albo nasluchiwanie zmian, moze lepiej nasluchiwanie zmian dla treningu
            friendsDao.insert(friends) //todo wyniesc to do view modelu, zeby tam byla rozpisana logika
            friends
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
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
                    }.addOnFailureListener { //todo dodac crashlytics
                    logD(it)
                    onFinishAction(R.string.cant_add_friend)
                }
            } else {
                onFinishAction(R.string.user_with_email_not_in_database)
            }
        }
    }
//todo jak ja dodam kogos do friends to mojemu znajomemu tez sie powinienem pokazac jako friend, a teraz tego nie ma
    private fun addFriendToFirestoreCollection(friend: Friend, onFinishAction: (Int) -> Unit) {
        firestore.collection(COLLECTION_USERS)
            .document(sharedPrefs.userEmail)
            .collection(COLLECTION_FRIENDS)
            .document(friend.id)
            .set(friend)
            .addOnSuccessListener {
                onFinishAction(R.string.friend_added)
            }.addOnFailureListener { //todo dodac crashlitics
                logD(it)
                onFinishAction(R.string.cant_add_friend)
            }
    }

    private fun User.toFriend() = Friend(id = id, email = email, name = name)

}