package com.zywczas.letsshare.fragments.history.domain

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.activitymain.domain.toDomain
import com.zywczas.letsshare.extentions.logD
import com.zywczas.letsshare.extentions.monthId
import com.zywczas.letsshare.models.GroupMonth
import com.zywczas.letsshare.models.GroupMonthDomain
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val firestoreRefs: FirestoreReferences,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    sharedPrefs: SharedPrefsWrapper
) : HistoryRepository {

    private val groupId = sharedPrefs.currentGroupId

    override suspend fun getPreviousMonths(): List<GroupMonthDomain>? =
        try {
            firestoreRefs.collectionGroupMonthsRefs(groupId)
                .orderBy(firestoreRefs.dateCreatedField, Query.Direction.DESCENDING)
                .get().await().toObjects<GroupMonth>()
                .filter { it.id != Date().monthId() }
                .map { it.toDomain() }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

}