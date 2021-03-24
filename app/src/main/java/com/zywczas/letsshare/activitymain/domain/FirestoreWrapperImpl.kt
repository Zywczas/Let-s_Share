package com.zywczas.letsshare.activitymain.domain

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.zywczas.letsshare.utils.COLLECTION_GROUPS
import com.zywczas.letsshare.utils.COLLECTION_MEMBERS
import javax.inject.Inject

class FirestoreWrapperImpl @Inject constructor(private val firestore: FirebaseFirestore) : FirestoreWrapper {

    override val members_num = "members_num"

    override suspend fun groupRef(groupId: String): DocumentReference =
        firestore.collection(COLLECTION_GROUPS).document(groupId)

    override suspend fun newGroupMemberRef(memberEmail: String, groupId: String): DocumentReference =
        firestore.collection(COLLECTION_GROUPS).document(groupId)
            .collection(COLLECTION_MEMBERS).document(memberEmail)

}