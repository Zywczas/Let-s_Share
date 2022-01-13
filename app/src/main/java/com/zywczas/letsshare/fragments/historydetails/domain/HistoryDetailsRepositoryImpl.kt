package com.zywczas.letsshare.fragments.historydetails.domain

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.extentions.logD
import com.zywczas.letsshare.models.Expense
import com.zywczas.letsshare.models.GroupMember
import com.zywczas.letsshare.models.firestore.ExpenseFire
import com.zywczas.letsshare.models.firestore.GroupMemberFire
import com.zywczas.letsshare.models.toDomain
import com.zywczas.letsshare.utils.wrappers.CrashlyticsWrapper
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import com.zywczas.letsshare.utils.wrappers.SharedPrefsWrapper
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HistoryDetailsRepositoryImpl @Inject constructor(
    private val firestoreRefs: FirestoreReferences,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    sharedPrefs: SharedPrefsWrapper
) : HistoryDetailsRepository {

    private val groupId = sharedPrefs.currentGroupId

    override suspend fun getMembers(monthId: String): List<GroupMember>? =
        try {
            firestoreRefs.collectionMembersRefs(groupId, monthId)
                .get().await()
                .toObjects<GroupMemberFire>().map { it.toDomain() }.sortedByDescending { it.expenses }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun getExpenses(monthId: String): List<Expense>? =
        try {
            firestoreRefs.collectionExpensesRefs(groupId, monthId)
                .orderBy(firestoreRefs.dateCreatedField, Query.Direction.DESCENDING)
                .get().await()
                .toObjects<ExpenseFire>().map { it.toDomain() }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun settleUpMonth(monthId: String): Int? =
        try {
            firestoreRefs.groupMonthRefs(groupId, monthId)
                .update(firestoreRefs.isSettledUpField, true).await()
            null
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_settle_up
        }

}