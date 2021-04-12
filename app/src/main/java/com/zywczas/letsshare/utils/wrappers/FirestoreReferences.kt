package com.zywczas.letsshare.utils.wrappers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

interface FirestoreReferences {

    val dateCreatedField: String
    val membersNumField: String
    val totalExpensesField: String
    val expensesField: String
    val valueField: String
    val groupsIdsField: String
    val percentageShareField: String

    suspend fun userRefs(email: String): DocumentReference

    suspend fun groupRefs(groupId: String): DocumentReference

    suspend fun collectionMembersRefs(groupId: String): CollectionReference

    fun groupMemberRefs(memberEmail: String, groupId: String): DocumentReference

    suspend fun groupMonthRefs(monthId: String, groupId: String): DocumentReference

    suspend fun collectionExpensesRefs(monthId: String, groupId: String): CollectionReference

    suspend fun newExpenseRefs(monthId: String, groupId: String): DocumentReference

}