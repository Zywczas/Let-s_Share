package com.zywczas.letsshare.fragments.friends.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.db.FriendsDao
import com.zywczas.letsshare.db.UserDao
import com.zywczas.letsshare.extentions.logD
import com.zywczas.letsshare.models.Friend
import com.zywczas.letsshare.models.firestore.FriendFire
import com.zywczas.letsshare.models.firestore.UserFire
import com.zywczas.letsshare.models.local.FriendLocal
import com.zywczas.letsshare.models.local.UserLocal
import com.zywczas.letsshare.utils.wrappers.CrashlyticsWrapper
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendsRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firestoreRefs: FirestoreReferences,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    private val friendsDao: FriendsDao,
    private val userDao: UserDao
) : FriendsRepository {

    private suspend fun getUser() = userDao.getUser()

    @ExperimentalCoroutinesApi
    override suspend fun getFriends(): Flow<List<Friend>> = callbackFlow {
        val listener = firestoreRefs.collectionFriends(getUser().id)
            .orderBy(firestoreRefs.nameField, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    channel.closeFlowAndThrow(error)
                }
                if (snapshot != null) {
                    offer(snapshot.toObjects<FriendFire>().map { it.toDomain() })
                }
            }
        awaitClose { listener.remove() }
    }

    private fun FriendFire.toDomain() = Friend(
        id = id,
        email = email,
        name = name
    )

    private fun <E> SendChannel<E>.closeFlowAndThrow(e: Exception){
        crashlyticsWrapper.sendExceptionToFirebase(e)
        logD(e)
        close(e)
    }

    override suspend fun saveFriendsLocally(friends: List<Friend>) = friendsDao.insert(friends.map { it.toLocal() })

    private fun Friend.toLocal() = FriendLocal(
        id = id,
        email = email,
        name = name
    )

    override suspend fun userEmail(): String = getUser().email

    override suspend fun addFriend(email: String): Int? =
        try {
            val singInMethods = firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods
            val isFriendRegistered = singInMethods?.size != null && singInMethods.size > 0
            if (isFriendRegistered){
                val newFriend = firestoreRefs.userQueryRefs(email).get().await()
                    .toObjects<UserFire>().first().toFriend()
                addFriendsToCollections(newFriend)
            } else { R.string.user_with_email_not_in_database }
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_friend
        }

    private fun UserFire.toFriend() = FriendFire(id = id, email = email, name = name)

    private suspend fun addFriendsToCollections(friend: FriendFire): Int? =
        try {
            val friendToUserCollectionRefs = firestoreRefs.friendRefs(getUser().id, friend.id)
            val userToFriendCollectionRefs = firestoreRefs.friendRefs(friend.id, getUser().id)
            val userAsFriend =userDao.getUser().toFriend()
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

    private fun UserLocal.toFriend() = FriendFire(id = id, email = email, name = name)

}