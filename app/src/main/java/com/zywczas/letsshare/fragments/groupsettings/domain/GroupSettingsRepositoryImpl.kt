package com.zywczas.letsshare.fragments.groupsettings.domain

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.toDomain
import com.zywczas.letsshare.model.*
import com.zywczas.letsshare.model.db.FriendsDao
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.wrappers.CrashlyticsWrapper
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import com.zywczas.letsshare.utils.wrappers.SharedPrefsWrapper
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

    override suspend fun getMembers(): List<GroupMemberDomain>? =
        try {
            firestoreRefs.collectionMembersRefs(groupId)
                .get().await()
                .toObjects<GroupMember>().map { it.toDomain() }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun getFriends(): List<Friend> = friendsDao.getFriends()

    override suspend fun isFriendInTheGroupAlready(newMemberEmail: String): Boolean? =
        getMembers()?.any { it.email == newMemberEmail }

    override suspend fun isFriendIn10GroupsAlready(newMemberEmail: String): Boolean? =
        try {
            firestoreRefs.userRefs(newMemberEmail).get().await()
                .toObject<User>()!!.groupsIds.size >= 10
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun addNewMemberIfBelow7InGroup(newMember: GroupMember): Int? =
        try  {
            val userToBeUpdatedRefs = firestoreRefs.userRefs(newMember.email)
            val groupRef = firestoreRefs.groupRefs(groupId)
            val newMemberRef = firestoreRefs.groupMemberRefs(newMember.email, groupId)

            firestore.runTransaction { transaction ->
                val group = transaction.get(groupRef).toObject<Group>()!!
                if (group.members_num < 7){
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

}