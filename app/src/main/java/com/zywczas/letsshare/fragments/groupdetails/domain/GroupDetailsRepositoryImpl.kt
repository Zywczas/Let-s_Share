package com.zywczas.letsshare.fragments.groupdetails.domain

import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.zywczas.letsshare.R
import com.zywczas.letsshare.model.*
import com.zywczas.letsshare.model.db.FriendsDao
import com.zywczas.letsshare.utils.logD
import com.zywczas.letsshare.utils.thisMonth
import com.zywczas.letsshare.utils.today
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
                .orderBy(firestoreRefs.expensesField, Query.Direction.DESCENDING)
                .get().await()
                .toObjects<GroupMember>().map { it.toDomain() }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            null
        }

    private fun GroupMember.toDomain() =
        GroupMemberDomain(name, email, expenses.toBigDecimal(), percentage_share.toBigDecimal())

    override suspend fun getFriends(): List<Friend> = friendsDao.getFriends()

    override suspend fun addNewMemberIfBelow7InGroup(newMember: GroupMember, groupId: String): Int? =
        try  {
            if (isMemberInTheGroupAlready(newMember.email, groupId)) {
                R.string.member_exists
            } else {
                val groupRef = firestoreRefs.groupRefs(groupId)
                val newMemberRef = firestoreRefs.groupMemberRefs(newMember.email, groupId)

                firestore.runTransaction { transaction ->
                    val group = transaction.get(groupRef).toObject<Group>()!!
                    if (group.members_num < 7){
                        transaction.set(newMemberRef, newMember)
                        transaction.update(groupRef, firestoreRefs.membersNumField, FieldValue.increment(1))
                        return@runTransaction null
                    } else {
                        return@runTransaction R.string.too_many_members
                    }
                }.await()
            }
        } catch (e: Exception) {
            crashlyticsWrapper.sendExceptionToFirebase(e)
            logD(e)
            R.string.cant_add_member
        }

    private suspend fun isMemberInTheGroupAlready(memberEmail: String, groupId: String): Boolean =
        getMembers(groupId)!!.firstOrNull{ it.email == memberEmail } != null

//            .orderBy(FIELD_NAME, Query.Direction.ASCENDING).orderBy(FIELD_TIME_CREATED, Query.Direction.DESCENDING)

    override suspend fun updateThisMonthAndAddNewExpense(groupId: String, name: String, amount: BigDecimal): Int? =
        try {
            val monthId = Date().thisMonth()
            val monthRefs = firestoreRefs.groupMonthRefs(monthId, groupId)
            val newExpenseRefs = firestoreRefs.newExpenseRefs(monthId, groupId)
            val newExpense = Expense(
                id = newExpenseRefs.id,
                name = name,
                payee_email = sharedPrefs.userEmail,
                payee_name = sharedPrefs.userName,
                value = amount.toString(),
                date_created = Date().today()
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
            logD(e)
        R.string.cant_add_expense
    }

}