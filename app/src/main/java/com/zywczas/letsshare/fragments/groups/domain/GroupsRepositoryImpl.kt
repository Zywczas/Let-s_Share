package com.zywczas.letsshare.fragments.groups.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.zywczas.letsshare.R
import com.zywczas.letsshare.utils.wrappers.CrashlyticsWrapper
import com.zywczas.letsshare.utils.wrappers.SharedPrefsWrapper
import com.zywczas.letsshare.model.Group
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.utils.*
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class GroupsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sharedPrefs: SharedPrefsWrapper,
    private val crashlyticsWrapper: CrashlyticsWrapper
) : GroupsRepository {

    private val fieldGroupsIds = "groupsIds"

    override suspend fun addGroupIfUserIsInLessThan10Groups(name: String, currency: String): Int =
        try {
            val userEmail = sharedPrefs.userEmail
            val newGroupRef = firestore.collection(COLLECTION_GROUPS).document()
            val newGroup = Group(
                id = newGroupRef.id,
                founder_email = userEmail,
                name = name,
                currency = currency,
                members_num = 1)
            val newGroupMemberRef = firestore
                .collection(COLLECTION_GROUPS).document(newGroup.id)
                .collection(COLLECTION_MEMBERS)
                .document(userEmail)
            val newMember = GroupMember(sharedPrefs.userName, userEmail)
            val userRef = firestore.collection(COLLECTION_USERS).document(userEmail)

            firestore.runTransaction { transaction ->
                val user = transaction.get(userRef).toObject<User>()!!
                val newGroupsIds: List<String> = when {
                    user.groupsIds.isEmpty() -> listOf(newGroup.id)
                    user.groupsIds.size < 10 -> user.groupsIds.plus(newGroup.id)
                    else -> return@runTransaction R.string.too_many_groups //todo sprawdzi czy to dobrze dziala, np dac ze moze byc tylko 2 grupy na chwile
                }
                transaction.set(newGroupRef, newGroup)
                transaction.set(newGroupMemberRef, newMember)
                transaction.update(userRef, fieldGroupsIds, newGroupsIds)
            }.await()
            R.string.group_added
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_group
        }

    override suspend fun getGroups(): List<Group>? =
        try {
            val user = firestore.collection(COLLECTION_USERS).document(sharedPrefs.userEmail)
                .get().await().toObject<User>()!!
            val groups = mutableListOf<Group>()
            user.groupsIds.forEach { id ->
                val group = firestore.collection(COLLECTION_GROUPS).document(id)
                    .get().await().toObject<Group>()!!
                groups.add(group)
            }
            groups.sortedByDescending { it.date_created }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

}