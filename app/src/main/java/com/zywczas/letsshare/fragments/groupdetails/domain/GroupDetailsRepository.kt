package com.zywczas.letsshare.fragments.groupdetails.domain

import com.zywczas.letsshare.model.GroupMember

interface GroupDetailsRepository {

    suspend fun getMembers(groupId: String): List<GroupMember>?

}