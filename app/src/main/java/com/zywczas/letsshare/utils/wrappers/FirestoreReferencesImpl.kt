package com.zywczas.letsshare.utils.wrappers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class FirestoreReferencesImpl @Inject constructor(private val firestore: FirebaseFirestore) : FirestoreReferences {

    override val dateCreatedField = "dateCreated"
    override val nameField = "name"
    override val membersNumField = "membersNum"
    override val totalExpensesField = "totalExpenses"
    override val expensesField = "expenses"
    override val valueField = "value"
    override val groupsIdsField = "groupsIds"
    override val percentageShareField = "share"
    override val isSettledUpField = "isSettledUp"
    override val messagingTokenField = "messagingToken"

    private val emailField = "email"
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

    override fun userQueryRefs(email: String): Query =
        firestore.collection(collectionUsers)
            .whereEqualTo(emailField, email)
            .limit(1)

    override fun friendRefs(userId: String, friendId: String): DocumentReference =
        firestore.collection(collectionUsers).document(userId)
            .collection(collectionFriends)
            .document(friendId)

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

    override fun newExpenseRefs(groupId: String, monthId: String): DocumentReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionExpenses)
            .document()

    override fun expenseRefs(groupId: String, monthId: String, expenseId: String): DocumentReference =
        firestore.collection(collectionGroups).document(groupId)
            .collection(collectionMonths).document(monthId)
            .collection(collectionExpenses)
            .document(expenseId)

}