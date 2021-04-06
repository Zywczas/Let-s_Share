package com.zywczas.letsshare.fragments.groupdetails.domain

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.model.*
import com.zywczas.letsshare.model.db.FriendsDao
import com.zywczas.letsshare.utils.*
import com.zywczas.letsshare.utils.wrappers.CrashlyticsWrapper
import com.zywczas.letsshare.utils.wrappers.FirestoreReferences
import com.zywczas.letsshare.utils.wrappers.SharedPrefsWrapper
import kotlinx.coroutines.tasks.await
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor(
    private val firestoreRefs: FirestoreReferences,
    private val firestore: FirebaseFirestore,
    private val crashlyticsWrapper: CrashlyticsWrapper,
    private val friendsDao: FriendsDao,
    private val sharedPrefs: SharedPrefsWrapper
) : GroupDetailsRepository {

    override suspend fun getMembers(groupId: String): List<GroupMemberDomain>? =
        try {
            firestoreRefs.collectionMembersRefs(groupId)
                .get().await()
                .toObjects<GroupMember>().map { it.toDomain() }.sortedByDescending { it.expenses }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    private fun GroupMember.toDomain() =
        GroupMemberDomain(name, email, expenses.toBigDecimal(), percentage_share.toBigDecimal())

    override suspend fun getFriends(): List<Friend> = friendsDao.getFriends()

    //todo getMembers moze rzucic nullem jak firestore cos nie pyknie, poprawic te funkcje
    override suspend fun isFriendInTheGroupAlready(memberEmail: String, groupId: String): Boolean =
        getMembers(groupId)!!.find{ it.email == memberEmail } != null

    override suspend fun isFriendIn10GroupsAlready(memberEmail: String): Boolean? = try {
        firestoreRefs.userRefs(memberEmail).get().await().toObject<User>()!!.groupsIds.size >= 10
    } catch (e: Exception){
        crashlyticsWrapper.sendExceptionToFirebase(e)
        logD(e)
        null
    }
//todo pamietac zeby przy nowym miesiac resetowac tez wydatki wszystkich czlonkow
    override suspend fun addNewMemberIfBelow7InGroup(member: GroupMember, groupId: String): Int? =
        try  {
            val userToBeUpdatedRefs = firestoreRefs.userRefs(member.email)
            val groupRef = firestoreRefs.groupRefs(groupId)
            val newMemberRef = firestoreRefs.groupMemberRefs(member.email, groupId)

            firestore.runTransaction { transaction ->
                val group = transaction.get(groupRef).toObject<Group>()!!
                if (group.members_num < 7){
                    transaction.set(newMemberRef, member)
                    transaction.update(groupRef, firestoreRefs.membersNumField, FieldValue.increment(1))
                    transaction.update(userToBeUpdatedRefs, firestoreRefs.groupsIdsField, FieldValue.arrayUnion(groupId))
                    return@runTransaction null
                } else {
                    return@runTransaction R.string.too_many_members
                }
            }.await()
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_member
        }

    override suspend fun getExpenses(groupId: String): List<ExpenseDomain>? =
        try {
            firestoreRefs.collectionExpensesRefs(Date().monthIdFormat(), groupId)
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

    override suspend fun updateThisMonthAndAddNewExpense(groupId: String, name: String, amount: BigDecimal): Int? =
        try {
            val monthId = Date().monthIdFormat()
            val monthRefs = firestoreRefs.groupMonthRefs(monthId, groupId)
            val newExpenseRefs = firestoreRefs.newExpenseRefs(monthId, groupId)
            val newExpense = Expense(
                id = newExpenseRefs.id,
                name = name,
                payee_email = sharedPrefs.userEmail,
                payee_name = sharedPrefs.userName,
                value = amount.toString(),
                date_created = dateInPoland()
            )
            val memberRef = firestoreRefs.groupMemberRefs(sharedPrefs.userEmail, groupId)
            firestore.runTransaction { transaction ->
                val month = transaction.get(monthRefs).toObject<GroupMonth>()
                val member = transaction.get(memberRef).toObject<GroupMember>()!!
                if (month != null){
                    val increasedMonthlyExpenses = (month.total_expenses.toBigDecimal() + amount).toString()
                    transaction.update(monthRefs, firestoreRefs.totalExpensesField, increasedMonthlyExpenses) //todo nie moze byc increament bo to bedzie string
                } else {
                    val newMonth = GroupMonth(monthId, groupId, amount.toString())
                    transaction.set(monthRefs, newMonth)
                }
                transaction.set(newExpenseRefs, newExpense)
                val increasedMemberExpenses = (member.expenses.toBigDecimal() + amount).toString()
                transaction.update(memberRef, firestoreRefs.expensesField, increasedMemberExpenses)
            }.await()
            null
        } catch (e: Exception){
            crashlyticsWrapper.sendExceptionToFirebase(e)
            e.printStackTrace()
            logD(e)
        R.string.cant_add_expense
    }

}