package com.zywczas.letsshare.utils.wrappers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

@Suppress("PropertyName")
interface FirestoreReferences {

    val members_num: String

    suspend fun groupRef(groupId: String): DocumentReference

    suspend fun collectionMembersRef(groupId: String): CollectionReference

    suspend fun newGroupMemberRef(memberEmail: String, groupId: String): DocumentReference

}