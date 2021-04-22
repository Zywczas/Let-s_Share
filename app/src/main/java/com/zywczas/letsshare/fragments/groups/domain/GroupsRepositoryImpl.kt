package com.zywczas.letsshare.fragments.groups.domain

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.Group
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMonth
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.utils.*
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class GroupsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firestoreRefs: FirestoreReferences,
    private val sharedPrefs: SharedPrefsWrapper,
    private val crashlyticsWrapper: CrashlyticsWrapper
) : GroupsRepository {

    private val userId = sharedPrefs.userId

    override suspend fun isUserIn5GroupsAlready(): Boolean? =
        try {
            firestoreRefs.userRefs(userId).get().await()
                .toObject<User>()!!.groupsIds.size >= 5
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun addGroup(name: String, currency: String): Int =
        try {
            val userEmail = sharedPrefs.userEmail
            val newGroupRef = firestoreRefs.newGroupRefs()
            val newGroup = Group(
                id = newGroupRef.id,
                name = name,
                currency = currency,
                membersNum = 1)
            val date = Date()
            val newMonthId = date.monthId()
            val newMonthRefs = firestoreRefs.groupMonthRefs(newGroupRef.id, newMonthId)
            val newMonth = GroupMonth(id = newMonthId, dateCreated = date)
            val newGroupMemberRef = firestoreRefs.groupMemberRefs(newGroupRef.id, newMonthId, userId)
            val newMember = GroupMember(id = userId, name = sharedPrefs.userName, email = userEmail)
            val userRef = firestoreRefs.userRefs(userId)

            firestore.runTransaction { transaction ->
                transaction.set(newGroupRef, newGroup)
                transaction.set(newMonthRefs, newMonth)
                transaction.set(newGroupMemberRef, newMember)
                transaction.update(userRef, firestoreRefs.groupsIdsField, FieldValue.arrayUnion(newGroup.id))
            }.await()
            R.string.group_added
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_group
        }

    override suspend fun getGroups(): List<Group>? =
        try {
            val user = firestoreRefs.userRefs(userId)
                .get().await()
                .toObject<User>()!!
            val groups = mutableListOf<Group>()
            user.groupsIds.forEach { id ->
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