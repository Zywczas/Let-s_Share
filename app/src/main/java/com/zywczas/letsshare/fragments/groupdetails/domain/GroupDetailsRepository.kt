package com.zywczas.letsshare.fragments.groupdetails.domain

import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember

interface GroupDetailsRepository {

    suspend fun getMembers(groupId: String): List<GroupMember>?

//    suspend fun saveCurrentGroupId(groupId: String)

    suspend fun getFriends(): List<Friend>

}