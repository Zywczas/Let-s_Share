package com.zywczas.letsshare.activitymain.domain

import com.google.firebase.firestore.DocumentReference

@Suppress("PropertyName")
interface FirestoreWrapper {

    val members_num: String

    suspend fun groupRef(groupId: String): DocumentReference

    suspend fun newGroupMemberRef(memberEmail: String, groupId: String): DocumentReference

}