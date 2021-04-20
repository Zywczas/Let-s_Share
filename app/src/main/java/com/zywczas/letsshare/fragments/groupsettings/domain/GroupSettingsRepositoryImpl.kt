package com.zywczas.letsshare.fragments.groupsettings.domain

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.*
import com.zywczas.letsshare.model.db.FriendsDao
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupSettingsRepositoryImpl @Inject constructor(
    private val firestoreRefs: FirestoreReferences,
    sharedPrefs: SharedPrefsWrapper,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    private val friendsDao: FriendsDao,
    private val firestore: FirebaseFirestore
) : GroupSettingsRepository {

    private val groupId = sharedPrefs.currentGroupId

    override suspend fun getMembers(monthId: String): List<GroupMemberDomain>? =
        try {
            firestoreRefs.collectionMembersRefs(groupId, monthId)
                .get().await().toObjects<GroupMember>().map { it.toDomain() }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    private fun GroupMember.toDomain() = GroupMemberDomain(
        id = id,
        name = name,
        email = email,
        expenses = expenses.toBigDecimal(),
        share = share.toBigDecimal()
    )

    override suspend fun getFriends(): List<Friend> = friendsDao.getFriends()

    override suspend fun isFriendIn5GroupsAlready(newMemberId: String): Boolean? =
        try {
            firestoreRefs.userRefs(newMemberId).get().await()
                .toObject<User>()!!.groupsIds.size >= 5
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun addMemberIfBelow7PeopleInGroup(monthId: String, friend: Friend): Int? =
        try {
            val newMember = friend.toGroupMember()
            val userToBeUpdatedRefs = firestoreRefs.userRefs(newMember.id)
            val groupRef = firestoreRefs.groupRefs(groupId)
            val newMemberRef = firestoreRefs.groupMemberRefs(groupId, monthId, newMember.id)

            firestore.runTransaction { transaction ->
                val group = transaction.get(groupRef).toObject<Group>()!!
                if (group.membersNum < 7) {
                    transaction.set(newMemberRef, newMember)
                    transaction.update(groupRef, firestoreRefs.membersNumField, FieldValue.increment(1))
                    transaction.update(userToBeUpdatedRefs, firestoreRefs.groupsIdsField, FieldValue.arrayUnion(groupId))
                    return@runTransaction null
                } else {
                    return@runTransaction R.string.too_many_members
                }
            }.await()
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_member
        }

    private fun Friend.toGroupMember() = GroupMember(id = id, name = name, email = email)

    override suspend fun saveSplits(monthId: String, members: List<GroupMemberDomain>): Int =
        try {
            val groupMembers = members.map { it.toGroupMember() }
            firestore.runBatch { batch ->
                groupMembers.forEach { member ->
                    batch.update(
                        firestoreRefs.groupMemberRefs(groupId, monthId, member.id),
                        firestoreRefs.percentageShareField,
                        member.share
                    )
                }
            }.await()
            R.string.save_success
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.something_wrong
        }

    private fun GroupMemberDomain.toGroupMember() = GroupMember(
        id = id,
        name = name,
        email = email,
        expenses = expenses.toString(),
        share = share.toString()
    )

}