package com.zywczas.letsshare.activitymain.domain

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Transaction

interface FirestoreReferences {

    val dateCreatedField: String
    val membersNumField: String
    val totalExpensesField: String
    val expensesField: String
    val valueField: String
    val groupsIdsField: String
    val percentageShareField: String

    fun userRefs(email: String): DocumentReference

    fun newGroupRefs(): DocumentReference

    fun groupRefs(groupId: String): DocumentReference

    fun collectionMembersRefs(groupId: String, monthId: String): CollectionReference

    fun groupMemberRefs(groupId: String, monthId: String, memberEmail: String): DocumentReference

    fun groupMonthRefs(groupId: String, monthId: String): DocumentReference

    fun collectionGroupMonthsRefs(groupId: String): CollectionReference

    fun collectionExpensesRefs(monthId: String, groupId: String): CollectionReference

    fun newExpenseRefs(monthId: String, groupId: String): DocumentReference

}