package com.zywczas.letsshare.fragments.groupdetails.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.utils.COLLECTION_GROUPS
import com.zywczas.letsshare.utils.COLLECTION_MEMBERS
import com.zywczas.letsshare.utils.FIELD_EXPENSES
import com.zywczas.letsshare.utils.logD
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val crashlyticsWrapper: CrashlyticsWrapper
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

}