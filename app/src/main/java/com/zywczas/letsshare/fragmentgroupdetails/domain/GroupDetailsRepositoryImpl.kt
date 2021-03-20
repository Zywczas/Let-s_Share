package com.zywczas.letsshare.fragmentgroupdetails.domain

import com.zywczas.letsshare.model.GroupMember
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor() : GroupDetailsRepository {
    override suspend fun getMembers(): List<GroupMember>? {
        return null
    }
}