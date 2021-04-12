package com.zywczas.letsshare.utils.wrappers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.zywczas.letsshare.utils.COLLECTION_GROUPS
import com.zywczas.letsshare.utils.COLLECTION_MEMBERS
import com.zywczas.letsshare.utils.COLLECTION_USERS
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import javax.inject.Inject

class FirestoreReferencesImpl @Inject constructor(private val firestore: FirebaseFirestore) : FirestoreReferences {
//todo moze przeniesc pozniej wszystkie const tutaj

    override val dateCreatedField = "date_created"
    override val membersNumField = "members_num"
    override val totalExpensesField = "total_expenses"
    override val expensesField = "expenses"
    override val valueField = "value"
    override val groupsIdsField = "groupsIds"
    override val percentageShareField = "percentage_share"

    private val collectionMonths = "months"
    private val collectionExpenses = "expenses"

    override suspend fun userRefs(email: String): DocumentReference =
        firestore.collection(COLLECTION_USERS)
            .document(email)

    override suspend fun groupRefs(groupId: String): DocumentReference =
        firestore.collection(COLLECTION_GROUPS)
            .document(groupId)

    override suspend fun collectionMembersRefs(groupId: String): CollectionReference =
        firestore.collection(COLLECTION_GROUPS).document(groupId)
            .collection(COLLECTION_MEMBERS)

    override fun groupMemberRefs(memberEmail: String, groupId: String): DocumentReference =
        firestore.collection(COLLECTION_GROUPS).document(groupId)
            .collection(COLLECTION_MEMBERS)
            .document(memberEmail)

    override suspend fun groupMonthRefs(monthId: String, groupId: String): DocumentReference =
        firestore.collection(COLLECTION_GROUPS).document(groupId)
            .collection(collectionMonths)
            .document(monthId)

    override suspend fun collectionExpensesRefs(monthId: String, groupId: String): CollectionReference =
        firestore.collection(COLLECTION_GROUPS).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionExpenses)

    override suspend fun newExpenseRefs(monthId: String, groupId: String): DocumentReference =
        firestore.collection(COLLECTION_GROUPS).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionExpenses)
            .document()

}