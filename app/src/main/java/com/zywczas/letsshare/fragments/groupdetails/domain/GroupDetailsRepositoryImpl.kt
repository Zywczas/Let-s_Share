package com.zywczas.letsshare.fragments.groupdetails.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.toDomain
import com.zywczas.letsshare.model.*
import com.zywczas.letsshare.utils.dateInPoland
import com.zywczas.letsshare.utils.dayFormat
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.monthId
import com.zywczas.letsshare.activitymain.domain.CrashlyticsWrapper
import com.zywczas.letsshare.activitymain.domain.FirestoreReferences
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
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

    override suspend fun getMonths(): List<GroupMonthDomain>? =
        try {
            firestoreRefs.collectionGroupMonthsRefs(groupId)
                .get().await()
                .toObjects<GroupMonth>().map { it.toDomain() }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }
//todo getMonths dziala, teraz reszta :)
    override suspend fun getMembers(groupId: String): List<GroupMemberDomain>? =
        try {
            firestoreRefs.collectionMembersRefs(groupId, Date().monthId()) //todo to zamienic na pobierane z view modelu
                .get().await()
                .toObjects<GroupMember>().map { it.toDomain() }.sortedByDescending { it.expenses }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    override suspend fun getMonth(): GroupMonthDomain? =
        try {
            firestoreRefs.groupMonthRefs("D2LepxGvWGTDC9hW8LQe", Date().monthId())
                .get().await()
                .toObject<GroupMonth>()?.toDomain() ?: GroupMonthDomain("-1")
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    private fun GroupMonth.toDomain() =
        GroupMonthDomain(id, groupId, total_expenses.toBigDecimal(), is_settled_up)

    override suspend fun getExpenses(groupId: String): List<ExpenseDomain>? =
        try {
            firestoreRefs.collectionExpensesRefs(Date().monthId(), groupId)
                .orderBy(firestoreRefs.dateCreatedField, Query.Direction.DESCENDING)
                .get().await()
                .toObjects<Expense>().map { it.toDomain() }
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    private fun Expense.toDomain() = ExpenseDomain(id, name, payee_email, payee_name,
        value.toBigDecimal(), date_created.dayFormat())

    //todo pamietac zeby przy nowym miesiac resetowac tez wydatki wszystkich czlonkow - teraz chyba nie resetuje
    //todo pomyslec czy ta nazwa jest adekwatna
    override suspend fun updateThisMonthAndAddNewExpense(groupId: String, name: String, amount: BigDecimal): Int? =
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
//                if (month != null){
//                    val increasedMonthlyExpenses = (month.total_expenses.toBigDecimal() + amount).toString()
//                    transaction.update(monthRefs, firestoreRefs.totalExpensesField, increasedMonthlyExpenses) //todo nie moze byc increament bo to bedzie string
//                } else {
//                    val newMonth = GroupMonth(monthId, groupId, amount.toString())
//                    transaction.set(monthRefs, newMonth)
//                }
//                transaction.set(newExpenseRefs, newExpense)
//                val increasedMemberExpenses = (member.expenses.toBigDecimal() + amount).toString()
//                transaction.update(memberRef, firestoreRefs.expensesField, increasedMemberExpenses)
//            }.await()
//            null
//        } catch (e: Exception){
//            crashlyticsWrapper.sendExceptionToFirebase(e)
//            e.printStackTrace()
//            logD(e)
        R.string.cant_add_expense
//    }

}