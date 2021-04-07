package com.zywczas.letsshare.fragments.groupsettings.domain

import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.activitymain.domain.toDomain
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMemberDomain
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.wrappers.CrashlyticsWrapper
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import com.zywczas.letsshare.utils.wrappers.SharedPrefsWrapper
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupSettingsRepositoryImpl @Inject constructor(
    private val firestoreRefs: FirestoreReferences,
    private val sharedPrefs: SharedPrefsWrapper,
    private val crashlyticsWrapper: CrashlyticsWrapper
) : GroupSettingsRepository {

    override suspend fun getMembers(): List<GroupMemberDomain>? =
        try {
            firestoreRefs.collectionMembersRefs(sharedPrefs.currentGroupId)
                .get().await()
                .toObjects<GroupMember>().map { it.toDomain() }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }


}