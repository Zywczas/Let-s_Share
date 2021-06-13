package com.zywczas.letsshare.fragments.history.domain

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.activitymain.domain.*
import com.zywczas.letsshare.extentions.logD
import com.zywczas.letsshare.models.GroupMonth
import com.zywczas.letsshare.models.GroupMonthDomain
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val firestoreRefs: FirestoreReferences,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    sharedPrefs: SharedPrefsWrapper,
    private val dateUtil: DateUtil
) : HistoryRepository {

    private val groupId = sharedPrefs.currentGroupId

    override suspend fun getPreviousMonths(): List<GroupMonthDomain>? =
        try {
            firestoreRefs.collectionGroupMonthsRefs(groupId)
                .orderBy(firestoreRefs.dateCreatedField, Query.Direction.DESCENDING)
                .get().await().toObjects<GroupMonth>()
                .filter { it.id != dateUtil.presentMonthId() }
                .map { it.toDomain() }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

}