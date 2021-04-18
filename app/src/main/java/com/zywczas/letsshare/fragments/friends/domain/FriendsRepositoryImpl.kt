package com.zywczas.letsshare.fragments.friends.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.model.db.FriendsDao
import com.zywczas.letsshare.utils.logD
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
            firestoreRefs.collectionFriends(userId)
                .orderBy(firestoreRefs.nameField, Query.Direction.ASCENDING)
                .get().await()
                .toObjects()        //todo pozniej to poprawic, dac najpierw pobieranie z bazy, potem z firestore, i wtedy update, albo dac swipe to refresh albo nasluchiwanie zmian, moze lepiej nasluchiwanie zmian dla treningu
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun saveFriendsLocally(friends: List<Friend>) = friendsDao.insert(friends)

    override suspend fun userEmail(): String = sharedPrefs.userEmail

    override suspend fun addFriend(email: String): Int? =
        try {
            val singInMethods = firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods
            val isFriendRegistered = singInMethods?.size != null && singInMethods.size > 0
            if (isFriendRegistered){
                val newFriend = firestoreRefs.userQueryRefs(email).get().await()
                    .toObjects<User>().first().toFriend()
                addFriendsToCollections(newFriend)
            } else { R.string.user_with_email_not_in_database }
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_friend
        }

    private fun User.toFriend() = Friend(id = id, email = email, name = name)

    private suspend fun addFriendsToCollections(friend: Friend): Int? =
        try {
            val friendToUserCollectionRefs = firestoreRefs.friendRefs(userId, friend.id)
            val userToFriendCollectionRefs = firestoreRefs.friendRefs(friend.id, userId)
            val userAsFriend = sharedPrefs.getLocalUser().toFriend()
            firestore.runBatch { batch ->
                batch.set(friendToUserCollectionRefs, friend)
                batch.set(userToFriendCollectionRefs, userAsFriend)
            }.await()
            null
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_friend
        }

}