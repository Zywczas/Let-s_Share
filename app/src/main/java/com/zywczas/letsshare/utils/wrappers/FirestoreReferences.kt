package com.zywczas.letsshare.utils.wrappers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

interface FirestoreReferences {

    val membersNumField: String

    suspend fun userRefs(email: String): DocumentReference

    suspend fun groupRefs(groupId: String): DocumentReference

    suspend fun collectionMembersRefs(groupId: String): CollectionReference

    suspend fun newGroupMemberRefs(memberEmail: String, groupId: String): DocumentReference

}