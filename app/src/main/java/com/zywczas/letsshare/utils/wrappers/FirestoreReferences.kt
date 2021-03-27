package com.zywczas.letsshare.utils.wrappers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

interface FirestoreReferences {

    val membersNumField: String
    val totalExpensesField: String
    val expensesField: String

    suspend fun userRefs(email: String): DocumentReference

    suspend fun groupRefs(groupId: String): DocumentReference

    suspend fun collectionMembersRefs(groupId: String): CollectionReference

    suspend fun groupMemberRefs(memberEmail: String, groupId: String): DocumentReference

    suspend fun groupMonthRefs(monthId: String, groupId: String): DocumentReference

    suspend fun collectionExpensesRefs(monthId: String, groupId: String): CollectionReference

    suspend fun newExpenseRefs(monthId: String, groupId: String): DocumentReference

//    suspend fun expenseRefs(monthId: String, groupId: String, expenseId: String): DocumentReference

}