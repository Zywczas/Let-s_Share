package com.zywczas.letsshare.fragmentgroupdetails.domain

import com.zywczas.letsshare.model.GroupMember

interface GroupDetailsRepository {

    suspend fun getMembers(groupId: String): List<GroupMember>?

}