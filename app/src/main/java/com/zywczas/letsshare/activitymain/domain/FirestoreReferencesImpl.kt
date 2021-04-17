package com.zywczas.letsshare.activitymain.domain

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
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

    private val collectionUsers = "users"
    private val collectionGroups = "groups"
    private val collectionMonths = "months"
    private val collectionMembers = "members"
    private val collectionExpenses = "expenses"

    override fun userRefs(email: String): DocumentReference =
        firestore.collection(collectionUsers)
            .document(email)
    
    override fun newGroupRefs(): DocumentReference =
        firestore.collection(collectionGroups).document()

    override fun groupRefs(groupId: String): DocumentReference =
        firestore.collection(collectionGroups)
            .document(groupId)

    override fun collectionMembersRefs(groupId: String, monthId: String): CollectionReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionMembers)

    override fun groupMemberRefs(groupId: String, monthId: String, memberEmail: String): DocumentReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionMembers)
            .document(memberEmail)

    override fun groupMonthRefs(groupId: String, monthId: String): DocumentReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths)
            .document(monthId)

    override fun collectionGroupMonthsRefs(groupId: String): CollectionReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths)

    override fun collectionExpensesRefs(groupId: String, monthId: String): CollectionReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionExpenses)

    override fun newExpenseRefs(monthId: String, groupId: String): DocumentReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionExpenses)
            .document()

}