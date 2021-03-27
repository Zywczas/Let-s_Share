package com.zywczas.letsshare.fragments.groupdetails.domain

import com.zywczas.letsshare.model.ExpenseDomain
import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMemberDomain
import java.math.BigDecimal

interface GroupDetailsRepository {

    suspend fun getMembers(groupId: String): List<GroupMemberDomain>?

    suspend fun getFriends(): List<Friend>

    suspend fun addNewMemberIfBelow7InGroup(newMember: GroupMember, groupId: String): Int?

    suspend fun getExpenses(groupId: String): List<ExpenseDomain>?

    suspend fun updateThisMonthAndAddNewExpense(groupId: String, name: String, amount: BigDecimal): Int?

}