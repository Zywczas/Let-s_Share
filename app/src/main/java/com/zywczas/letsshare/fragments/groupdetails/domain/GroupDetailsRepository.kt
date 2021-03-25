package com.zywczas.letsshare.fragments.groupdetails.domain

import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember
import java.math.BigDecimal

interface GroupDetailsRepository {

    suspend fun getMembers(groupId: String): List<GroupMember>?

    suspend fun getFriends(): List<Friend>

    suspend fun addNewMemberIfBelow7InGroup(newMember: GroupMember, groupId: String): Int?

    suspend fun addNewExpense(groupId: String, name: String, amount: BigDecimal): Int?

}