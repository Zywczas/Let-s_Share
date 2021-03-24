package com.zywczas.letsshare.fragments.groupdetails.domain

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreWrapper
import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.Group
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.db.FriendsDao
import com.zywczas.letsshare.utils.COLLECTION_GROUPS
import com.zywczas.letsshare.utils.COLLECTION_MEMBERS
import com.zywczas.letsshare.utils.FIELD_EXPENSES
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor(
    private val firestoreWrapper: FirestoreWrapper,
    private val firestore: FirebaseFirestore,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    private val friendsDao: FriendsDao
) : GroupDetailsRepository {

    override suspend fun getMembers(groupId: String): List<GroupMember>? =
        try {
            firestore.collection(COLLECTION_GROUPS).document(groupId)
                .collection(COLLECTION_MEMBERS)
                .orderBy(FIELD_EXPENSES, Query.Direction.DESCENDING)
                .get().await()
                .toObjects()
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun getFriends(): List<Friend> = friendsDao.getFriends()

    override suspend fun addNewMemberIfBelow7InGroup(newMember: GroupMember, groupId: String): Int? =
        try  {
            if (isMemberInTheGroupAlready(newMember.email, groupId)) {
                R.string.member_exists
            } else {
                val groupRef = firestoreWrapper.groupRef(groupId)
                val newMemberRef = firestoreWrapper.newGroupMemberRef(newMember.email, groupId)

                firestore.runTransaction { transaction ->
                    val group = transaction.get(groupRef).toObject<Group>()!!
                    if (group.members_num < 7){
                        transaction.set(newMemberRef, newMember)
                        transaction.update(groupRef, firestoreWrapper.members_num, FieldValue.increment(1))
                        return@runTransaction null
                    } else {
                        return@runTransaction R.string.too_many_members
                    }
                }.await()
            }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_member
        }

    private suspend fun isMemberInTheGroupAlready(memberEmail: String, groupId: String): Boolean =
        getMembers(groupId)!!.firstOrNull{ it.email == memberEmail } != null

}