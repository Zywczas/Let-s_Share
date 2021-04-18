package com.zywczas.letsshare.activitymain.domain

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.zywczas.letsshare.utils.COLLECTION_FRIENDS
import com.zywczas.letsshare.utils.COLLECTION_USERS
import javax.inject.Inject

class FirestoreReferencesImpl @Inject constructor(private val firestore: FirebaseFirestore) : FirestoreReferences {
//todo przeniesc pozniej wszystkie const tutaj

    override val dateCreatedField = "dateCreated"
    override val nameField = "name"
    override val membersNumField = "membersNum"
    override val totalExpensesField = "totalExpenses"
    override val expensesField = "expenses"
    override val valueField = "value"
    override val groupsIdsField = "groupsIds"
    override val percentageShareField = "share"

    private val collectionUsers = "users"
    private val collectionFriends = "friends"
    private val collectionGroups = "groups"
    private val collectionMonths = "months"
    private val collectionMembers = "members"
    private val collectionExpenses = "expenses"

    override fun userRefs(id: String): DocumentReference =
        firestore.collection(collectionUsers)
            .document(id)

    override fun collectionFriends(userId: String): CollectionReference =
        firestore.collection(collectionUsers)
            .document(userId)
            .collection(collectionFriends)

    override fun newGroupRefs(): DocumentReference =
        firestore.collection(collectionGroups).document()

    override fun groupRefs(groupId: String): DocumentReference =
        firestore.collection(collectionGroups)
            .document(groupId)

    override fun collectionMembersRefs(groupId: String, monthId: String): CollectionReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionMembers)

    override fun groupMemberRefs(groupId: String, monthId: String, memberId: String): DocumentReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionMembers)
            .document(memberId)

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