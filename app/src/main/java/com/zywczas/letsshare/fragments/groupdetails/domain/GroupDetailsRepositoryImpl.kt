package com.zywczas.letsshare.fragments.groupdetails.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.activitymain.domain.toDomain
import com.zywczas.letsshare.models.*
import com.zywczas.letsshare.utils.dateInPoland
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.monthId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor(
    private val firestoreRefs: FirestoreReferences,
    private val firestore: FirebaseFirestore,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    private val sharedPrefs: SharedPrefsWrapper
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

    @ExperimentalCoroutinesApi
    override suspend fun listenToMonth(monthId: String): Flow<GroupMonthDomain> = callbackFlow {
        val listener = firestoreRefs.groupMonthRefs(groupId, monthId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    channel.closeFlowAndThrow(error)
                }
                if (snapshot != null && snapshot.exists()){
                    offer(snapshot.toObject<GroupMonth>()!!.toDomain())
                }
            }
        awaitClose { listener.remove() }
    }

    private fun <E> SendChannel<E>.closeFlowAndThrow(e: Exception){
        crashlyticsWrapper.sendExceptionToFirebase(e)
        logD(e)
        close(e)
    }

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

    override suspend fun createNewMonth(members: List<GroupMemberDomain>): Int? =
        try {
            val date = Date()
            val monthId = date.monthId()
            val newMonthRefs = firestoreRefs.groupMonthRefs(groupId, monthId)
            val newMonth = GroupMonth(id = monthId, dateCreated = date)
            val newMonthMembers = members.map { it.toNewMonthGroupMember() }

            firestore.runBatch { batch ->
                batch.set(newMonthRefs, newMonth)
                newMonthMembers.forEach { member ->
                    val memberRefs = firestoreRefs.groupMemberRefs(groupId, monthId, member.id)
                    batch.set(memberRefs, member)
                }
            }.await()
            null
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_get_month
        }

    private fun GroupMemberDomain.toNewMonthGroupMember() = GroupMember(
        id = id,
        name = name,
        email = email,
        share = share.toString()
    )

    override suspend fun addExpense(monthId: String, name: String, amount: BigDecimal): Int? =
        try {
            val monthRefs = firestoreRefs.groupMonthRefs(groupId, monthId)
            val newExpenseRefs = firestoreRefs.newExpenseRefs(groupId, monthId)
            val newExpense = Expense(
                id = newExpenseRefs.id,
                name = name,
                payeeId = sharedPrefs.userId,
                payeeName = sharedPrefs.userName,
                value = amount.toString(),
                dateCreated = dateInPoland()
            )
            val memberRef = firestoreRefs.groupMemberRefs(groupId, monthId, sharedPrefs.userId)

            firestore.runTransaction { transaction ->
                val month = transaction.get(monthRefs).toObject<GroupMonth>()!!
                val member = transaction.get(memberRef).toObject<GroupMember>()!!
                val increasedMonthlyExpenses =
                    (month.totalExpenses.toBigDecimal() + amount).toString()
                val increasedMemberExpenses = (member.expenses.toBigDecimal() + amount).toString()
                transaction.update(
                    monthRefs,
                    firestoreRefs.totalExpensesField,
                    increasedMonthlyExpenses
                )
                transaction.update(memberRef, firestoreRefs.expensesField, increasedMemberExpenses)
                transaction.set(newExpenseRefs, newExpense)
            }.await()
            null
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_expense
        }

    override suspend fun deleteExpense(monthId: String, expense: ExpenseDomain): Int? =
        try {
            val monthRefs = firestoreRefs.groupMonthRefs(groupId, monthId)
            val memberRef = firestoreRefs.groupMemberRefs(groupId, monthId, expense.payeeId)
            val expenseRef = firestoreRefs.expenseRefs(groupId, monthId, expense.id)

            firestore.runTransaction { transaction ->
                val month = transaction.get(monthRefs).toObject<GroupMonth>()!!
                val member = transaction.get(memberRef).toObject<GroupMember>()
                val decreasedMonthlyExpenses =
                    (month.totalExpenses.toBigDecimal() - expense.value).toString()
                transaction.update(
                    monthRefs,
                    firestoreRefs.totalExpensesField,
                    decreasedMonthlyExpenses
                )
                transaction.delete(expenseRef)
                if (member != null){
                    val decreasedMemberExpenses = (member.expenses.toBigDecimal() - expense.value).toString()
                    transaction.update(memberRef, firestoreRefs.expensesField, decreasedMemberExpenses)
                }
            }.await()
            null
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_delete_expense
        }

    override suspend fun getUserName(): String = sharedPrefs.userName

}