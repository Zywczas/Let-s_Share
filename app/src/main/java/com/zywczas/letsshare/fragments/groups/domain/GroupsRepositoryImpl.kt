package com.zywczas.letsshare.fragments.groups.domain

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.DateUtil
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.db.UserDao
import com.zywczas.letsshare.extentions.logD
import com.zywczas.letsshare.extentions.monthId
import com.zywczas.letsshare.models.Group
import com.zywczas.letsshare.models.GroupMember
import com.zywczas.letsshare.models.GroupMonth
import com.zywczas.letsshare.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firestoreRefs: FirestoreReferences,
    private val sharedPrefs: SharedPrefsWrapper,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    private val userDao: UserDao,
    private val dateUtil: DateUtil
) : GroupsRepository {

    override suspend fun isUserIn5GroupsAlready(): Boolean? =
        try {
            firestoreRefs.userRefs(userDao.getUser().id).get().await()
                .toObject<User>()!!.groupsIds.size > 4
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun addGroup(name: String, currency: String): Int? =
        try {
            val user = userDao.getUser()
            val newGroupRef = firestoreRefs.newGroupRefs()
            val newGroup = Group(
                id = newGroupRef.id,
                name = name,
                currency = currency,
                membersNum = 1)
            val date = dateUtil.presentDate()
            val newMonthId = date.monthId()
            val newMonthRefs = firestoreRefs.groupMonthRefs(newGroupRef.id, newMonthId)
            val newMonth = GroupMonth(id = newMonthId, dateCreated = date)
            val newGroupMemberRef = firestoreRefs.groupMemberRefs(newGroupRef.id, newMonthId, user.id)
            val newMember = GroupMember(id = user.id, name = user.name, email = user.email)
            val userRef = firestoreRefs.userRefs(user.id)

            firestore.runBatch { batch ->
                batch.set(newGroupRef, newGroup)
                batch.set(newMonthRefs, newMonth)
                batch.set(newGroupMemberRef, newMember)
                batch.update(userRef, firestoreRefs.groupsIdsField, FieldValue.arrayUnion(newGroup.id))
            }.await()
            null
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_group
        }

    @ExperimentalCoroutinesApi
    override suspend fun getUser(): Flow<User> = callbackFlow {
        val listener = firestoreRefs.userRefs(userDao.getUser().id)
            .addSnapshotListener { snapshot, error ->
            if (error != null) {
                channel.closeFlowAndThrow(error)
            }
            if (snapshot != null && snapshot.exists()) {
                offer(snapshot.toObject()!!)
            }
        }
        awaitClose { listener.remove() }
    }

    private fun <E> SendChannel<E>.closeFlowAndThrow(e: Exception){
        crashlyticsWrapper.sendExceptionToFirebase(e)
        logD(e)
        close(e)
    }

    override suspend fun getGroups(groupsIds: List<String>): List<Group>? =
        try {
            val groups = mutableListOf<Group>()
            groupsIds.forEach { id ->
                val group = firestoreRefs.groupRefs(id)
                    .get().await()
                    .toObject<Group>()!!
                groups.add(group)
            }
            groups.sortedBy { it.name }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun saveCurrentlyOpenGroupId(groupId: String) {
        sharedPrefs.currentGroupId = groupId
    }

}