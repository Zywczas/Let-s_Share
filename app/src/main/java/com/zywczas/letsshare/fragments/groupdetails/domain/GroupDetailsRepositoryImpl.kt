package com.zywczas.letsshare.fragments.groupdetails.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.*
import com.zywczas.letsshare.utils.dayFormat
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.monthId
import kotlinx.coroutines.tasks.await
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor(
    private val firestoreRefs: FirestoreReferences,
    private val firestore: FirebaseFirestore,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    sharedPrefs: SharedPrefsWrapper
) : GroupDetailsRepository {

    private val groupId = sharedPrefs.currentGroupId

    override suspend fun getLastMonth(): GroupMonthDomain? =
        try {
            firestoreRefs.collectionGroupMonthsRefs(groupId)
                .orderBy(firestoreRefs.dateCreatedField, Query.Direction.DESCENDING)
                .get().await().toObjects<GroupMonth>()
                .takeIf { it.isNotEmpty() }?.first()?.toDomain() ?: GroupMonthDomain()
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    private fun GroupMonth.toDomain() = GroupMonthDomain(
        id = id,
        totalExpenses = total_expenses.toBigDecimal(),
        isSettledUp = is_settled_up
    )

    override suspend fun getMembers(monthId: String): List<GroupMemberDomain>? =
        try {
            firestoreRefs.collectionMembersRefs(groupId, monthId)
                .get().await()
                .toObjects<GroupMember>().map { it.toDomain() }.sortedByDescending { it.expenses }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    private fun GroupMember.toDomain() = GroupMemberDomain(
        name = name,
        email = email,
        expenses = expenses.toBigDecimal(),
        percentageShare = percentage_share.toBigDecimal()
    )

    override suspend fun getExpenses(monthId: String): List<ExpenseDomain>? =
        try {
            firestoreRefs.collectionExpensesRefs(groupId, monthId)
                .orderBy(firestoreRefs.dateCreatedField, Query.Direction.DESCENDING)
                .get().await()
                .toObjects<Expense>().map { it.toDomain() }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    private fun Expense.toDomain() = ExpenseDomain(
        id = id,
        name = name,
        payeeEmail = payee_email,
        payeeName = payee_name,
        value = value.toBigDecimal(),
        dateCreated = date_created.dayFormat()
    )

    override suspend fun createNewMonth(members: List<GroupMemberDomain>): Int? =
        try {
            val date = Date()
            val monthId = date.monthId()
            val newMonthRefs = firestoreRefs.groupMonthRefs(groupId, monthId)
            val newMonth = GroupMonth(id = monthId, date_created = date)
            val newMonthMembers = members.map { it.toGroupMember() }
            firestore.runBatch { batch ->
                batch.set(newMonthRefs, newMonth)
                newMonthMembers.forEach { member ->
                    val memberRefs = firestoreRefs.groupMemberRefs(groupId, monthId, member.email)
                    batch.set(memberRefs, member)
                }
            }.await()
            null
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_get_month
        }

    private fun GroupMemberDomain.toGroupMember() = GroupMember(
        name = name,
        email = email,
        percentage_share = percentageShare.toString()
    )

    //todo pomyslec czy ta nazwa jest adekwatna
    override suspend fun updateThisMonthAndAddNewExpense(groupId: String,name: String,amount: BigDecimal): Int? =
//        try {
//            val monthId = Date().monthId()
//            val monthRefs = firestoreRefs.groupMonthRefs(groupId, monthId)
//            val newExpenseRefs = firestoreRefs.newExpenseRefs(monthId, groupId)
//            val newExpense = Expense(
//                id = newExpenseRefs.id,
//                name = name,
//                payee_email = sharedPrefs.userEmail,
//                payee_name = sharedPrefs.userName,
//                value = amount.toString(),
//                date_created = dateInPoland()
//            )
//            val memberRef = firestoreRefs.groupMemberRefs(groupId, sharedPrefs.userEmail)
//            firestore.runTransaction { transaction ->
//                val month = transaction.get(monthRefs).toObject<GroupMonth>()
//                val member = transaction.get(memberRef).toObject<GroupMember>()!!
//                if (month != null) {
//                    val increasedMonthlyExpenses = (month.total_expenses.toBigDecimal() + amount).toString()
//                    transaction.update(
//                        monthRefs,
//                        firestoreRefs.totalExpensesField,
//                        increasedMonthlyExpenses
//                    ) //todo nie moze byc increament bo to bedzie string
//                } else {
//                    val newMonth = GroupMonth(monthId, amount.toString())
//                    transaction.set(monthRefs, newMonth)
//                }
//                transaction.set(newExpenseRefs, newExpense)
//                val increasedMemberExpenses = (member.expenses.toBigDecimal() + amount).toString()
//                transaction.update(memberRef, firestoreRefs.expensesField, increasedMemberExpenses)
//            }.await()
//            null
//        } catch (e: Exception) {
//            crashlyticsWrapper.sendExceptionToFirebase(e)
//            e.printStackTrace()
//            logD(e)
            R.string.cant_add_expense
//        }

}