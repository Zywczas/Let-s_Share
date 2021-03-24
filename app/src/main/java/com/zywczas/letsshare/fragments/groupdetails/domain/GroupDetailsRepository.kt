package com.zywczas.letsshare.fragments.groupdetails.domain

import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember

interface GroupDetailsRepository {

    suspend fun getMembers(groupId: String): List<GroupMember>?

    suspend fun getFriends(): List<Friend>

    suspend fun addNewMemberIfBelow7InGroup(newMember: GroupMember, groupId: String): Int?

}