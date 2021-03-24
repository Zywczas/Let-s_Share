package com.zywczas.letsshare.utils.wrappers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.zywczas.letsshare.utils.COLLECTION_GROUPS
import com.zywczas.letsshare.utils.COLLECTION_MEMBERS
import com.zywczas.letsshare.utils.COLLECTION_USERS
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import javax.inject.Inject

class FirestoreReferencesImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    FirestoreReferences {

    override val membersNumField = "members_num"

    override suspend fun userRefs(email: String): DocumentReference =
        firestore.collection(COLLECTION_USERS)
            .document(email)

    override suspend fun groupRefs(groupId: String): DocumentReference =
        firestore.collection(COLLECTION_GROUPS)
            .document(groupId)

    override suspend fun collectionMembersRefs(groupId: String): CollectionReference =
        firestore.collection(COLLECTION_GROUPS).document(groupId)
            .collection(COLLECTION_MEMBERS)

    override suspend fun newGroupMemberRefs(memberEmail: String, groupId: String): DocumentReference =
        firestore.collection(COLLECTION_GROUPS).document(groupId)
            .collection(COLLECTION_MEMBERS)
            .document(memberEmail)

}